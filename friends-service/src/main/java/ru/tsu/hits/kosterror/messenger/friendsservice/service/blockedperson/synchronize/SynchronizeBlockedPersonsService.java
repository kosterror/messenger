package ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.synchronize;

import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;

import javax.transaction.Transactional;
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
    @Transactional
    void syncBlockedPersonIdFullName(UUID blockedPersonId);

    /**
     * Метод для синхронизации информации о заблокированных пользователях.
     *
     * @param personDto актуальная информация о пользователе.
     */
    @Transactional
    void syncBlockedPerson(PersonDto personDto);

}
