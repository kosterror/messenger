package ru.tsu.hits.kosterror.messenger.chatservice.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.SendMessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Chat;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Message;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.RelationPerson;
import ru.tsu.hits.kosterror.messenger.chatservice.repository.MessageRepository;
import ru.tsu.hits.kosterror.messenger.chatservice.service.chatinfo.ChatInfoService;
import ru.tsu.hits.kosterror.messenger.chatservice.service.person.RelationPersonService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final RelationPersonService relationPersonService;
    private final MessageRepository messageRepository;
    private final ChatInfoService chatInfoService;

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

}
