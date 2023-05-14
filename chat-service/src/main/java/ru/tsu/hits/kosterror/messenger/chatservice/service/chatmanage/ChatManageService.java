package ru.tsu.hits.kosterror.messenger.chatservice.service.chatmanage;

import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.CreateChatDto;

import java.util.UUID;

public interface ChatManageService {
    ChatDto createGroupChat(UUID adminId, CreateChatDto dto);

}
