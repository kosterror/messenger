package ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.display;

import ru.tsu.hits.kosterror.messenger.core.dto.BooleanDto;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.BlockedPersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.request.BlockedPersonBasicFilters;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.request.BlockedPersonFilters;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс, который предоставляет методы для получения черного списка какого-то пользователя.
 */
public interface DisplayBlockedPersonService {

    BlockedPersonDto getBlockedPerson(UUID ownerId, UUID memberId);

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

    /**
     * Метод для проверки нахождения в черном списке у пользователя с
     * {@code id = ownerId} пользователя с {@code id = memberId}.
     *
     * @param ownerId  кто потенциально добавил в черный список.
     * @param memberId тот, кто потенциально находится там.
     * @return находится ли пользователь с {@code id = memberId} в черном списке у пользователя с {@code id = ownerId}.
     */
    BooleanDto personIsBlocked(
            UUID ownerId,
            UUID memberId
    );

    /**
     * Метод для поиска пользователей, среди черного списка.
     *
     * @param userId  идентификатор целевого пользователя.
     * @param request информация о пагинации и фильтрации.
     * @return список заблокированных пользователей с информацией о пагинации.
     */
    PagingResponse<List<BlockedPersonDto>> searchBlockedPersons(UUID userId,
                                                                PagingFilteringRequest<BlockedPersonFilters> request);
}
