package ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.display;

import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.FriendDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.request.FriendBasicFilters;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс, который предоставляет методы для получения друзей какого-то пользователя.
 */
public interface DisplayFriendService {

    /**
     * Метод для получения информации о друге целевого пользователя.
     *
     * @param ownerId  идентификатор целевого пользователя.
     * @param memberId идентификатор внешнего пользователя.
     * @return информация о внешнем пользователе.
     */
    FriendDto getFriend(UUID ownerId,
                        UUID memberId);

    /**
     * Метод для получения друзей пользователя.
     *
     * @param userId  идентификатор пользователя.
     * @param request информация о пагинации и фильтрации.
     * @return список друзей с информацией о пагинации.
     */
    PagingResponse<List<FriendDto>> getFriends(UUID userId, PagingFilteringRequest<FriendBasicFilters> request);

}
