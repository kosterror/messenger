package ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.synchronize;

import java.util.UUID;

/**
 * Интерфейс, предоставляющий методы для синхронизации данных заблокированных пользователей.
 */
public interface SynchronizeBlockedPersonsService {

    /**
     * Метод для обновления ФИО во всех записях, где внешний пользователь имеет id = {@code blockedPersonId}.
     *
     * @param blockedPersonId идентификатор внешнего пользователя.
     */
    void syncBlockedPersonIdFullName(UUID blockedPersonId);

}
