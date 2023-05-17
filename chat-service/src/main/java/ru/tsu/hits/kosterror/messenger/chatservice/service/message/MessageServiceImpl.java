package ru.tsu.hits.kosterror.messenger.chatservice.service.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.SendMessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Chat;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Message;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.RelationPerson;
import ru.tsu.hits.kosterror.messenger.chatservice.enumeration.ChatType;
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

    @Override
    @Transactional
    public void sendMessageToGroupChat(UUID authorId, SendMessageDto dto) {
        Chat chat = chatInfoService.findChatEntityById(authorId, dto.getTargetId());
        RelationPerson author = relationPersonService.findRelationPersonEntity(authorId);

        //TODO добавить attachments
        Message messageToSend = new Message(
                dto.getText(),
                LocalDateTime.now(),
                chat,
                null,
                author
        );

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

        //TODO добавить attachments
        Message messageToSave = Message
                .builder()
                .text(dto.getText())
                .sendingDate(LocalDateTime.now())
                .chat(chat)
                .attachments(null)
                .relationPerson(author)
                .build();

        messageToSave = messageRepository.save(messageToSave);
        log.info("Сообщение отправлено: {}", messageToSave);
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
        } catch (Exception exception) {
            throw new InternalException("Ошибка во время интеграционного запроса в friends-service", exception);
        }
    }

}
