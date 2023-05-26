package ru.tsu.hits.kosterror.messenger.chatservice.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.MessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.SendMessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Attachment;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Chat;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Message;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.RelationPerson;
import ru.tsu.hits.kosterror.messenger.chatservice.enumeration.ChatType;
import ru.tsu.hits.kosterror.messenger.chatservice.mapper.MessageMapper;
import ru.tsu.hits.kosterror.messenger.chatservice.repository.MessageRepository;
import ru.tsu.hits.kosterror.messenger.chatservice.service.attachment.AttachmentService;
import ru.tsu.hits.kosterror.messenger.chatservice.service.chatinfo.ChatInfoService;
import ru.tsu.hits.kosterror.messenger.chatservice.service.chatmanage.ChatManageService;
import ru.tsu.hits.kosterror.messenger.chatservice.service.person.RelationPersonService;
import ru.tsu.hits.kosterror.messenger.core.dto.NewNotificationDto;
import ru.tsu.hits.kosterror.messenger.core.dto.PairPersonIdDto;
import ru.tsu.hits.kosterror.messenger.core.enumeration.NotificationType;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.exception.ForbiddenException;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.integration.friends.FriendIntegrationService;
import ru.tsu.hits.kosterror.messenger.core.util.RabbitMQBindings;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Класс, реализующий {@link MessageService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private static final int NOTIFICATION_TEXT_SIZE = 100;
    private final RelationPersonService relationPersonService;
    private final MessageRepository messageRepository;
    private final ChatInfoService chatInfoService;
    private final ChatManageService chatManageService;
    private final FriendIntegrationService friendIntegrationService;
    private final MessageMapper messageMapper;
    private final AttachmentService attachmentService;
    private final StreamBridge streamBridge;

    @Override
    @Transactional
    public void sendMessageToChat(UUID authorId, SendMessageDto dto) {
        Chat chat = chatInfoService.findChatEntityById(authorId, dto.getTargetId());
        RelationPerson author = relationPersonService.findRelationPersonEntity(authorId);
        Message message = buildMessage(chat, author, dto);

        message = messageRepository.save(message);
        sendNewMessageNotification(chat, message, author);
    }

    @Override
    @Transactional
    public void sendMessageToPerson(UUID authorId, SendMessageDto dto) {
        if (authorId.equals(dto.getTargetId())) {
            throw new BadRequestException("Нельзя отправить сообщение самому себе");
        }

        checkBlocking(authorId, dto.getTargetId());

        RelationPerson author = relationPersonService
                .findOptionalRelationPerson(authorId)
                .orElseGet(() -> {
                    log.info("Сущность RelationPerson для отправителя не найдена, поэтому будет создана");
                    return relationPersonService.createRelationPersonEntity(authorId);
                });

        RelationPerson receiver = relationPersonService
                .findOptionalRelationPerson(dto.getTargetId())
                .orElseGet(() -> {
                    log.info("Сущность RelationPerson для получателя не найдена, поэтому будет создана");
                    return relationPersonService.createRelationPersonEntity(dto.getTargetId());
                });

        Chat chat = findPrivateChat(author, receiver)
                .orElseGet(() -> {
                    var createdPrivateChat = chatManageService.createPrivateChat(author, receiver);
                    log.info("Личный диалог не был найден, но успешно создан");
                    return createdPrivateChat;
                });

        Message message = buildMessage(chat, author, dto);
        message = messageRepository.save(message);
        log.info("Сообщение отправлено: {}", message);
        sendNewMessageNotification(chat, message, author);
    }

    @Override
    public List<MessageDto> getChatMessages(UUID personId, UUID chatId) {
        Chat chat = chatInfoService.findChatEntityById(personId, chatId);
        List<Message> messages = chat.getMessages();
        sortMessagesBySendingDate(messages);

        return messages
                .stream()
                .map(messageMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageDto> findMessages(UUID personId, String substring) {
        RelationPerson relationPerson = relationPersonService.findRelationPersonEntity(personId);
        List<Chat> chats = relationPerson.getChats();

        List<Message> rightMessages = new ArrayList<>();

        for (Chat chat : chats) {
            List<Message> unfilteredMessages = chat.getMessages();
            List<Message> filteredMessages = filterMessages(unfilteredMessages, substring);
            rightMessages.addAll(filteredMessages);
        }

        sortMessagesBySendingDate(rightMessages);

        return rightMessages
                .stream()
                .map(messageMapper::entityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Метод для создания объекта {@link Message} для отправки сообщения.
     *
     * @param chat   сущность чата.
     * @param author сущность автора.
     * @param dto    информация об отправляемом сообщении.
     * @return созданный объект.
     */
    private Message buildMessage(Chat chat, RelationPerson author, SendMessageDto dto) {
        Message message = Message.builder()
                .text(dto.getText())
                .sendingDate(LocalDateTime.now())
                .chat(chat)
                .relationPerson(author)
                .build();

        List<Attachment> attachments = buildAttachments(message, author.getPersonId(), dto);
        message.setAttachments(attachments);
        return message;
    }

    /**
     * Метод для построения списка вложений сообщения.
     *
     * @param message  сущность сообщения.
     * @param authorId идентификатор автора.
     * @param dto      информация об отправляемом сообщении.
     * @return список построенных вложений.
     */
    private List<Attachment> buildAttachments(Message message, UUID authorId, SendMessageDto dto) {
        List<Attachment> attachments = new ArrayList<>();
        List<UUID> nonexistentFileIds = new ArrayList<>();
        List<UUID> fileIds = dto.getAttachmentIds();

        if (dto.getAttachmentIds() == null) {
            return attachments;
        }

        for (UUID fileId : fileIds) {
            try {
                Attachment attachment = attachmentService.saveAttachment(message, fileId, authorId);
                attachments.add(attachment);
            } catch (NotFoundException exception) {
                nonexistentFileIds.add(fileId);
            }
        }

        if (!nonexistentFileIds.isEmpty()) {
            throw new NotFoundException("Файлы с id: '" + nonexistentFileIds + "' не существуют");
        }

        return attachments;
    }

    /**
     * Метод для фильтрации списка сообщений по подстроке.
     *
     * @param messages  список сообщений.
     * @param substring подстрока.
     * @return отфильтрованный список.
     */
    private List<Message> filterMessages(List<Message> messages, String substring) {
        String substringUpper = substring.toUpperCase();

        return messages
                .stream()
                .filter(msg -> {
                    if (msg.getText() != null && msg.getText().toUpperCase().contains(substringUpper)) {
                        return true;
                    }
                    return msg.getAttachments() != null && msg.getAttachments()
                            .stream()
                            .anyMatch(attachment -> attachment.getName() != null &&
                                    attachment.getName().toUpperCase().contains(substringUpper));
                }).collect(Collectors.toList());
    }

    /**
     * Сортирует сообщения по дате отправки.
     *
     * @param messages список сообщений, который будет отсортирован.
     */
    private void sortMessagesBySendingDate(List<Message> messages) {
        messages.sort((msg1, msg2) -> {
            if (msg1 == null || msg2 == null) {
                throw new NullPointerException("Коллекция с элементами null не может быть отсортирована");
            }

            LocalDateTime sendDate1 = msg1.getSendingDate();
            LocalDateTime sendDate2 = msg2.getSendingDate();

            if (sendDate1.isAfter(sendDate2)) {
                return -1;
            } else if (sendDate1.isBefore(sendDate2)) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    /**
     * Ищет личный чат между пользователями.
     *
     * @param first  сущность первого пользователя.
     * @param second сущность второго пользователя.
     * @return сущность чата, обернутая в {@link Optional}.
     */
    private Optional<Chat> findPrivateChat(RelationPerson first, RelationPerson second) {
        List<Chat> firstChats = getPrivateChats(first);
        List<Chat> crossedChats = findChatsWithMember(firstChats, second.getId());

        if (crossedChats.isEmpty()) {
            log.info(
                    "Не найден диалог между двумя пользователями: {}, {}",
                    first.getPersonId(),
                    second.getPersonId()
            );
            return Optional.empty();
        } else if (crossedChats.size() == 1) {
            log.info(
                    "Найден единственный личный диалог между двумя пользователями: {}, {}",
                    first.getPersonId(),
                    second.getPersonId()
            );
            return Optional.of(crossedChats.get(0));
        } else {
            log.error(
                    "Аномалия данных. У пользователей {} и {} несколько общий чатов",
                    first.getPersonId(),
                    second.getPersonId()
            );
            return Optional.of(crossedChats.get(0));
        }
    }

    /**
     * Метод для получения списка чатов, которые являются личными для пользователя.
     *
     * @param relationPerson сущность пользователя, который получает список личных чатов.
     * @return список личных чатов.
     */
    private List<Chat> getPrivateChats(RelationPerson relationPerson) {
        return relationPerson
                .getChats()
                .stream()
                .filter(chat -> chat.getType().equals(ChatType.PRIVATE))
                .collect(Collectors.toList());
    }

    /**
     * Получает список сущностей чатов с конкретным участником.
     *
     * @param chats    список чатов, в котором нужно отфильтровать данные.
     * @param memberId идентификатор участника чата.
     * @return список чатов с заданным участником.
     */
    private List<Chat> findChatsWithMember(List<Chat> chats, UUID memberId) {
        return chats
                .stream()
                .filter(chat -> chat
                        .getMembers()
                        .stream()
                        .anyMatch(member -> member.getId().equals(memberId))
                )
                .collect(Collectors.toList());
    }

    /**
     * Проверяет заблокирован ли кто-то из собеседников другим.`
     *
     * @param authorId   пользователь, который отправляет сообщение.
     * @param receiverId пользователь, который получает сообщение.
     */
    private void checkBlocking(UUID authorId, UUID receiverId) {
        PairPersonIdDto first = new PairPersonIdDto(authorId, receiverId);
        PairPersonIdDto second = new PairPersonIdDto(receiverId, authorId);

        try {
            Boolean receiverIsBlocked = friendIntegrationService.checkPersonInfoIsBlocked(first).getValue();
            Boolean authorIsBlocked = friendIntegrationService.checkPersonInfoIsBlocked(second).getValue();
            if (Boolean.TRUE.equals(authorIsBlocked)) {
                throw new ForbiddenException("Вы в черном списке. " +
                        "Вам запрещено отправлять этому пользователю сообщения");
            }

            if (Boolean.TRUE.equals(receiverIsBlocked)) {
                throw new BadRequestException("Пользователь у вас в черном списке");
            }
        } catch (RestClientException exception) {
            throw new InternalException("Ошибка во время интеграционного запроса в friends-service", exception);
        }
    }

    /**
     * Метод для получения собеседника в личном чате.
     *
     * @param privateChat сущность личного чата.
     * @param authorId    id пользователя, собеседника которого нужно найти.
     * @return найденный собеседник.
     */
    private RelationPerson findReceiver(Chat privateChat, UUID authorId) {
        if (privateChat.getType() != ChatType.PRIVATE) {
            throw new InternalException("Попытка найти собеседника в чате, который не является личным");
        }

        if (privateChat.getMembers() == null || privateChat.getMembers().size() != 2) {
            throw new InternalException("Некорректное количество участников в личном чате");
        }

        return privateChat
                .getMembers()
                .stream()
                .filter(member -> !member.getPersonId().equals(authorId))
                .findFirst()
                .orElseThrow(() -> new InternalException("Собеседник в личном чате не найден"));
    }

    /**
     * Отправляет уведомление через брокер о новом сообщении.
     *
     * @param chat    сущность чата, где пришло новое сообщение.
     * @param message сущность сообщения.
     * @param author  сущность автора сообщения.
     */
    private void sendNewMessageNotification(Chat chat, Message message, RelationPerson author) {
        if (chat.getType() != ChatType.PRIVATE) {
            return;
        }

        RelationPerson receiver = findReceiver(chat, author.getPersonId());

        String notificationText = String.format("Сообщение от: %s.", message.getRelationPerson().getFullName());

        if (message.getText() != null && !message.getText().isBlank()) {
            notificationText += message.getText();
            if (notificationText.length() > NOTIFICATION_TEXT_SIZE) {
                notificationText = notificationText.substring(0, NOTIFICATION_TEXT_SIZE);
            }
        }

        NewNotificationDto newNotificationDto = new NewNotificationDto(receiver.getPersonId(),
                NotificationType.NEW_MESSAGE,
                notificationText
        );

        streamBridge.send(RabbitMQBindings.CREATE_NOTIFICATION_OUT, newNotificationDto);
    }

}
