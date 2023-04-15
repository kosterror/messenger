package ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.manage;

import ru.tsu.hits.kosterror.messenger.coresecurity.model.JwtPersonData;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.CreateFriendDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.FriendDto;

import java.util.UUID;

/**
 * Интерфейс с методами для управления друзьями.
 */
public interface FriendManageService {

    /**
     * Метод для добавления пользователя в друзья.
     *
     * @param owner  информация о пользователе, который добавляет в друзья.
     * @param member информация о пользователе, которого добавляют в друзья.
     * @return информация о сохраненном друге.
     */
    FriendDto createFriend(JwtPersonData owner, CreateFriendDto member);

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
