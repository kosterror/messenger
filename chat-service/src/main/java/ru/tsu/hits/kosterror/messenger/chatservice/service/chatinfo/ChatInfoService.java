package ru.tsu.hits.kosterror.messenger.chatservice.service.chatinfo;

import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Chat;

import java.util.UUID;

public interface ChatInfoService {

    Chat findChatEntityById(UUID personId, UUID chatId);

    ChatDto findChatById(UUID personId, UUID chatId);

}
