package ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.manage;

import java.util.UUID;

/**
 * Интерфейс с методами для управления черным списком.
 */
public interface BlockedPersonManageService {

    /**
     * Метод для удаления человека из черного списка.
     *
     * @param ownerId  id пользователя, который удаляет.
     * @param memberId id пользователя, которого удаляют.
     */
    void deleteBlockedPerson(UUID ownerId, UUID memberId);

}
