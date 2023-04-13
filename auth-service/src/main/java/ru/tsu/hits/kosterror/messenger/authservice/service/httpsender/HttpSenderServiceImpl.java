package ru.tsu.hits.kosterror.messenger.authservice.service.httpsender;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.tsu.hits.kosterror.messenger.core.constant.Url;
import ru.tsu.hits.kosterror.messenger.core.dto.PairPersonIdDto;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.coresecurity.util.Constants;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Сервис, для выполнения интеграционных запросов.
 */
@Service
@Slf4j
public class HttpSenderServiceImpl implements HttpSenderService {

    private static final String REQUEST_ERROR = "Ошибка во время выполнения интеграционного запроса" +
            "с URL = '%s'";

    @Value("${app.security.integration.api-key}")
    private String apiKey;

    @Override
    public ResponseEntity<Object> friendsServicePersonIsBlocked(PairPersonIdDto body) {
        try {
            URI uri = new URI(Url.API_GATEWAY_PATH + Url.FRIENDS_PERSON_IS_BLOCKED);
            HttpHeaders headers = buildHeaders();
            HttpEntity<PairPersonIdDto> entityBody = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            log.info(entityBody.toString());
            return restTemplate.postForEntity(uri, entityBody, Object.class);

        } catch (URISyntaxException exception) {
            log.error("Не удалось создать URI из частей: '{}'+'{}'",
                    Url.API_GATEWAY_PATH,
                    Url.FRIENDS_PERSON_IS_BLOCKED,
                    exception
            );
            throw new InternalException(String.format(REQUEST_ERROR,
                    Url.API_GATEWAY_PATH + Url.FRIENDS_PERSON_IS_BLOCKED)
            );
        } catch (Exception exception) {
            log.error(String.format(REQUEST_ERROR, Url.API_GATEWAY_PATH + Url.FRIENDS_PERSON_IS_BLOCKED),
                    exception
            );
            throw new InternalException(String.format(REQUEST_ERROR,
                    Url.API_GATEWAY_PATH + Url.FRIENDS_PERSON_IS_BLOCKED)
            );
        }
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set(Constants.HEADER_API_KEY, apiKey);

        return headers;
    }

}
