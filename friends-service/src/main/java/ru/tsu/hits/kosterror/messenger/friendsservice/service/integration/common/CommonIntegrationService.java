package ru.tsu.hits.kosterror.messenger.friendsservice.service.integration.common;

import org.springframework.http.HttpHeaders;

/**
 * Интерфейс, предоставляющий методы с общей логикой
 * для интеграционных запросов.
 */
public interface CommonIntegrationService {

    /**
     * Метод для создания хэдеров для интеграционных запросов.
     *
     * @return хэдеры интеграционных запросов.
     */
    HttpHeaders buildHeaders();

}
