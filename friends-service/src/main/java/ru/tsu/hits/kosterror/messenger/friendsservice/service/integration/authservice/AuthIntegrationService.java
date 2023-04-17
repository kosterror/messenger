package ru.tsu.hits.kosterror.messenger.friendsservice.service.integration.authservice;

import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;

import java.util.UUID;

/**
 * Интерфейс с методами, которые посылают интеграционные запросы в auth-service.
 */
public interface AuthIntegrationService {

    /**
     * Метод для отправки интеграционного запроса на получение информации о профиле пользователя.
     *
     * @param personId идентификатор пользователя.
     * @return информация о пользователе. Если статус код 200, то вернется {@link PersonDto}
     */
    PersonDto getPersonInfo(UUID personId);

}
