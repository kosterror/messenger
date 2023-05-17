package ru.tsu.hits.kosterror.messenger.chatservice.service.message;

import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.SendMessageDto;

import java.util.UUID;

public interface MessageService {

    @Transactional
    void sendMessageToGroupChat(UUID authorId, SendMessageDto dto);

    @Transactional
    void sendMessageToPrivateChat(UUID authorId, SendMessageDto dto);

}
