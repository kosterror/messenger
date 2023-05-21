package ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.manage;

import ru.tsu.hits.kosterror.messenger.friendsservice.dto.FriendDto;

import java.util.UUID;

/**
 * Интерфейс с методами для управления друзьями.
 */
public interface ManageFriendService {

    FriendDto createFriend(UUID ownerId, UUID memberId);

    /**
     * Метод для удаления друга.
     *
     * @param ownerId  id пользователя, который удаляет.
     * @param memberId id пользователя, которого удаляют.
     */
    void deleteFriend(UUID ownerId, UUID memberId);

    /**
     * Метод для проверки существования дружбы хотя бы с одной стороны.
     *
     * @param firstId  идентификатор первого пользователя.
     * @param secondId идентификатор другого пользователя.
     * @return существует ли сущность дружбы хотя бы с одной стороны.
     */
    boolean isFriends(UUID firstId, UUID secondId);

}
