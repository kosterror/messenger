package ru.tsu.hits.kosterror.messenger.authservice.service.httpsender;

import org.springframework.http.ResponseEntity;
import ru.tsu.hits.kosterror.messenger.core.dto.PairPersonIdDto;

/**
 * Интерфейс, предоставляющий методы, которые скрывают в себе
 * интеграционные запросы в другие сервисы.
 */
public interface HttpSenderService {

    /**
     * Запрос в {@code friends-service} на получение информации: заблокирован ли один пользователь другим.
     *
     * @param requestBody тело запроса.
     * @return ответ, обернутый в {@code ResponseEntity}.
     */
    ResponseEntity<Object> friendsServicePersonIsBlocked(PairPersonIdDto requestBody);

}
