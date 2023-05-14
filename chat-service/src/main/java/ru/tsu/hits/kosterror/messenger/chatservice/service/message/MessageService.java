package ru.tsu.hits.kosterror.messenger.chatservice.service.message;

import ru.tsu.hits.kosterror.messenger.chatservice.dto.SendMessageDto;

import java.util.UUID;

public interface MessageService {

    void sendMessageToGroupChat(UUID authorId, SendMessageDto dto);

}
