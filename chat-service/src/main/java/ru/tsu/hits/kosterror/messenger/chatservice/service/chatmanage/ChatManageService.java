package ru.tsu.hits.kosterror.messenger.chatservice.service.chatmanage;

import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.CreateUpdateChatDto;

import java.util.UUID;

public interface ChatManageService {
    ChatDto createGroupChat(UUID adminId, CreateUpdateChatDto dto);

}
