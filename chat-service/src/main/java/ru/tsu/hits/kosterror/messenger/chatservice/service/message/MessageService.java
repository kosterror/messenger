package ru.tsu.hits.kosterror.messenger.chatservice.service.message;

import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.MessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.SendMessageDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    @Transactional
    void sendMessageToChat(UUID authorId, SendMessageDto dto);

    @Transactional
    void sendMessageToPerson(UUID authorId, SendMessageDto dto);

    List<MessageDto> getChatMessages(UUID personId, UUID chatId);

    List<MessageDto> findMessages(UUID personId, String substring);

}
