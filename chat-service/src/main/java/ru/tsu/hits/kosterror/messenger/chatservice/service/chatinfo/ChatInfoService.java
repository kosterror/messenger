package ru.tsu.hits.kosterror.messenger.chatservice.service.chatinfo;

import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatDto;

import java.util.UUID;

public interface ChatInfoService {

    ChatDto findChatById(UUID personId, UUID chatId);

}
