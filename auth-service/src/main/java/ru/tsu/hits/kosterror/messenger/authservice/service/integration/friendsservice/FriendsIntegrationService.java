package ru.tsu.hits.kosterror.messenger.authservice.service.integration.friendsservice;

import org.springframework.web.client.RestClientException;
import ru.tsu.hits.kosterror.messenger.core.dto.BooleanDto;

import java.util.UUID;

/**
 * Интерфейс с интеграционными запросами в friends-service.
 */
public interface FriendsIntegrationService {

    /**
     * Метод для проверки нахождения в черном списке.
     *
     * @param ownerId  пользователь, который потенциально добавил в черный список.
     * @param memberId пользователь, который потенциально может оказаться в черном списке.
     * @return объект с информацией о том, находится ли пользователь memberId в черном списке у ownerId.
     * @throws RestClientException возникает при некорректном запросе.
     */
    BooleanDto checkPersonIsBlocked(UUID ownerId, UUID memberId);

}
