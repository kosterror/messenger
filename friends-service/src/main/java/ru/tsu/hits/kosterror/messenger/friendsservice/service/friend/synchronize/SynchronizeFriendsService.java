package ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.synchronize;

import ru.tsu.hits.kosterror.messenger.friendsservice.entity.Friend;

import java.util.UUID;

/**
 * Интерфейс предоставляющий методы для синхронизации информации в сущностях {@link Friend}.
 */
public interface SynchronizeFriendsService {

    /**
     * Метод для обновления ФИО во всех записях, где внешний пользователь имеет id = {@code friendId}.
     *
     * @param friendId идентификатор внешнего пользователя.
     */
    void syncFriendFullName(UUID friendId);

}
