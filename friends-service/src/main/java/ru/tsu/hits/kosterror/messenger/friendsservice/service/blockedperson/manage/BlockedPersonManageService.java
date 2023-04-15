package ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.manage;

import ru.tsu.hits.kosterror.messenger.friendsservice.dto.BlockedPersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.CreateBlockedPersonDto;

import java.util.UUID;

/**
 * Интерфейс с методами для управления черным списком.
 */
public interface BlockedPersonManageService {

    /**
     * Метод для добавления пользователя в чёрный список.
     *
     * @param ownerId   идентификатор пользователя, который добавляет другого пользователя в чёрный список.
     * @param memberDto информация о пользователе, которого блокируют.
     */
    BlockedPersonDto createBlockedPerson(UUID ownerId, CreateBlockedPersonDto memberDto);

    /**
     * Метод для удаления человека из черного списка.
     *
     * @param ownerId  id пользователя, который удаляет.
     * @param memberId id пользователя, которого удаляют.
     */
    void deleteBlockedPerson(UUID ownerId, UUID memberId);
}
