package ru.tsu.hits.kosterror.messenger.chatservice.service.chatinfo;

import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatFilters;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatMessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Chat;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;

import java.util.List;
import java.util.UUID;

public interface ChatInfoService {

    Chat findChatEntityById(UUID personId, UUID chatId);

    ChatDto findChatById(UUID personId, UUID chatId);

    PagingResponse<List<ChatMessageDto>> getChats(UUID personId, PagingFilteringRequest<ChatFilters> request);

}
