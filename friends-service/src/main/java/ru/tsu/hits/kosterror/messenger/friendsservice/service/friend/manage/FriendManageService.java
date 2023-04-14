package ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.manage;

import java.util.UUID;

/**
 * Интерфейс с методами для управления друзьями.
 */
public interface FriendManageService {

    /**
     * Метод для удаления друга.
     *
     * @param ownerId  id пользователя, который удаляет.
     * @param memberId id пользователя, которого удаляют.
     */
    void deleteFriend(UUID ownerId, UUID memberId);

}
