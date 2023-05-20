package ru.tsu.hits.kosterror.messenger.chatservice.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.MessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.SendMessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.*;
import ru.tsu.hits.kosterror.messenger.chatservice.enumeration.ChatType;
import ru.tsu.hits.kosterror.messenger.chatservice.mapper.MessageMapper;
import ru.tsu.hits.kosterror.messenger.chatservice.repository.MessageRepository;
import ru.tsu.hits.kosterror.messenger.chatservice.service.chatinfo.ChatInfoService;
import ru.tsu.hits.kosterror.messenger.chatservice.service.chatmanage.ChatManageService;
import ru.tsu.hits.kosterror.messenger.chatservice.service.person.RelationPersonService;
import ru.tsu.hits.kosterror.messenger.core.dto.PairPersonIdDto;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.exception.ForbiddenException;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.core.integration.friends.FriendIntegrationService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final RelationPersonService relationPersonService;
    private final MessageRepository messageRepository;
    private final ChatInfoService chatInfoService;
    private final ChatManageService chatManageService;
    private final FriendIntegrationService friendIntegrationService;
    private final MessageMapper messageMapper;
    private final AttachmentService attachmentService;

    @Override
    @Transactional
    public void sendMessageToGroupChat(UUID authorId, SendMessageDto dto) {
        Chat chat = chatInfoService.findChatEntityById(authorId, dto.getTargetId());
        RelationPerson author = relationPersonService.findRelationPersonEntity(authorId);

        Message messageToSend = Message.builder()
                .text(dto.getText())
                .sendingDate(LocalDateTime.now())
                .chat(chat)
                .relationPerson(author)
                .build();
        List<Attachment> attachments = buildAttachments(messageToSend, authorId, dto);
        messageToSend.setAttachments(attachments);

        messageRepository.save(messageToSend);
    }

    @Override
    @Transactional
    public void sendMessageToPrivateChat(UUID authorId, SendMessageDto dto) {
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

        Message messageToSave = Message
                .builder()
                .text(dto.getText())
                .sendingDate(LocalDateTime.now())
                .chat(chat)
                .relationPerson(author)
                .build();

        List<Attachment> attachments = buildAttachments(messageToSave, authorId, dto);
        messageToSave.setAttachments(attachments);

        messageToSave = messageRepository.save(messageToSave);
        log.info("Сообщение отправлено: {}", messageToSave);
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

    private List<Chat> getPrivateChats(RelationPerson relationPerson) {
        return relationPerson
                .getChats()
                .stream()
                .filter(chat -> chat.getType().equals(ChatType.PRIVATE))
                .collect(Collectors.toList());
    }

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

}
