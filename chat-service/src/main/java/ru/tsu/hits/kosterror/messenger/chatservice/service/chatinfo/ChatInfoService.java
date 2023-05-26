package ru.tsu.hits.kosterror.messenger.chatservice.service.chatinfo;

import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatFilters;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatMessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Chat;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс предоставляющий методы для получения сущностей {@link Chat}.
 */
public interface ChatInfoService {

    /**
     * Находит сущность чата по идентификатору, и проверяет доступ.
     *
     * @param personId пользователь, который пытается получить доступ к чату.
     * @param chatId   идентификатор чата.
     * @return сущность чата.
     */
    Chat findChatEntityById(UUID personId, UUID chatId);

    /**
     * Находит чат по идентификатору, и проверяет доступ.
     *
     * @param personId пользователь, который пытается получить доступ к чату.
     * @param chatId   идентификатор чата.
     * @return информация о чате.
     */
    ChatDto findChatById(UUID personId, UUID chatId);

    /**
     * Получает список чатов пользователя с учетом параметров пагинации и фильтрации.
     *
     * @param personId идентификатор пользователя.
     * @param request  параметры пагинации и фильтрации.
     * @return найденная информация.
     */
    PagingResponse<List<ChatMessageDto>> getChats(UUID personId, PagingFilteringRequest<ChatFilters> request);

}
