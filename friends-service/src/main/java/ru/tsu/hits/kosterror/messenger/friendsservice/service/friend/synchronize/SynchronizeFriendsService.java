package ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.synchronize;

import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.Friend;

import javax.transaction.Transactional;
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
    @Transactional
    void syncFriendFullName(UUID friendId);

    @Transactional
    void syncFriend(PersonDto personDto);

}
