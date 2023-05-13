package ru.tsu.hits.kosterror.messenger.core.integration.friends;

import org.springframework.web.client.RestClientException;
import ru.tsu.hits.kosterror.messenger.core.dto.BooleanDto;
import ru.tsu.hits.kosterror.messenger.core.dto.PairPersonIdDto;

/**
 * Интерфейс с интеграционными запросами в friends-service.
 */
public interface FriendIntegrationService {

    /**
     * Метод для проверки нахождения в черном списке.
     *
     * @param dto объект, в котором находятся идентификаторы пользователей.
     * @return объект с информацией о том, находится ли пользователь memberId в черном списке у ownerId.
     * @throws RestClientException возникает при некорректном запросе.
     */
    BooleanDto checkPersonInfoIsBlocked(PairPersonIdDto dto);

    /**
     * Метод для проверки существования двунаправленной дружбы.
     *
     * @param dto объект, в котором находятся идентификаторы пользователей.бы.
     * @return объект с информацией о том являются пользователи друзьями или нет.
     */
    BooleanDto checkIsFriends(PairPersonIdDto dto);

}
