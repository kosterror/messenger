package ru.tsu.hits.kosterror.messenger.chatservice.service.chatmanage;

import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.CreateUpdateChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Chat;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.RelationPerson;

import java.util.UUID;

public interface ChatManageService {
    Chat createPrivateChat(RelationPerson first, RelationPerson second);

    ChatDto createGroupChat(UUID adminId, CreateUpdateChatDto dto);

    ChatDto updateGroupChat(UUID adminId, UUID chatId, CreateUpdateChatDto dto);

}
