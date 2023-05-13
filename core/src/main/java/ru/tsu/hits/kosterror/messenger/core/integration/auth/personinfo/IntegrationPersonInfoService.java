package ru.tsu.hits.kosterror.messenger.core.integration.auth.personinfo;

import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;

import java.util.UUID;

/**
 * Интерфейс с методами для получения информации о пользователе с помощью отправки интеграционных
 * запросов в auth-service.
 */
public interface IntegrationPersonInfoService {

    /**
     * Метод для отправки интеграционного запроса на получение информации о профиле пользователя.
     *
     * @param personId идентификатор пользователя.
     * @return информация о пользователе. Если статус код 200, то вернется {@link PersonDto}.
     */
    PersonDto getPersonInfo(UUID personId);

}
