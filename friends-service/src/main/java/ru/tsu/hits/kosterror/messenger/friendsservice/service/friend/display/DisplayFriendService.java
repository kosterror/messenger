package ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.display;

import ru.tsu.hits.kosterror.messenger.core.dto.BooleanDto;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.FriendDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.request.FriendBasicFilters;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.request.FriendFilters;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс, который предоставляет методы для получения друзей какого-то пользователя.
 */
public interface DisplayFriendService {

    /**
     * Метод для проверки существования двунаправленной связи дружбы между пользователями.
     *
     * @param ownerId  идентификатор первого пользователя.
     * @param memberId идентификатор второго пользователя.
     * @return существует ли связь дружбы.
     */
    BooleanDto isFriends(UUID ownerId, UUID memberId);

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

    /**
     * Метод для поиска по друзьям пользователя.
     *
     * @param userId  идентификатор пользователя.
     * @param request информация о фильтрации и пагинации.
     * @return список друзей с информацией о пагинации.
     */
    PagingResponse<List<FriendDto>> searchFriends(UUID userId,
                                                  PagingFilteringRequest<FriendFilters> request
    );
}
