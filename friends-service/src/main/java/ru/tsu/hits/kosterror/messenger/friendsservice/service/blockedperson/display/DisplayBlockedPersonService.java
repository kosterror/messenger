package ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.display;

import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.BlockedPersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.request.BlockedPersonBasicFilters;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс, который предоставляет методы для получения черного списка какого-то пользователя.
 */
public interface DisplayBlockedPersonService {

    /**
     * Метод для получения списка заблокированных пользователей.
     *
     * @param userId  идентификатор пользователя.
     * @param request информация о пагинации и фильтрации.
     * @return список заблокированных пользователей с информацией о пагинации.
     */
    PagingResponse<List<BlockedPersonDto>> getBlockedPersons(
            UUID userId,
            PagingFilteringRequest<BlockedPersonBasicFilters> request
    );

}
