package ru.tsu.hits.kosterror.messenger.friendsservice.service.integrationservice.authservice;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.tsu.hits.kosterror.messenger.coresecurity.util.Constants;

import java.util.List;
import java.util.UUID;

/**
 * Класс, реализующий {@link AuthIntegrationService}.
 */
@Service
@RequiredArgsConstructor
public class AuthIntegrationServiceImpl implements AuthIntegrationService {

    @Value("${app.integration-endpoints.get-person-info}")
    private String personInfoUrl;

    @Value("${app.security.integration.api-key}")
    private String apiKey;

    @Override
    public ResponseEntity<Object> getPersonInfo(UUID personId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = buildHeaders();
        HttpEntity<Object> request = new HttpEntity<>(header);
        return restTemplate.exchange(
                personInfoUrl,
                HttpMethod.GET,
                request,
                Object.class,
                personId
        );
    }

    /**
     * Метод для создания HttpHeaders с заданными параметрами.
     *
     * @return HttpHeaders с заданным Content-Type, Accept и заголовком API-ключа.
     */
    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set(Constants.HEADER_API_KEY, apiKey);

        return headers;
    }

}
