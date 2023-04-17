package ru.tsu.hits.kosterror.messenger.friendsservice.service.integration.authservice;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.integration.common.CommonIntegrationService;

import java.util.UUID;

/**
 * Класс, реализующий {@link AuthIntegrationService}.
 */
@Service
@RequiredArgsConstructor
public class AuthIntegrationServiceImpl implements AuthIntegrationService {

    @Value("${app.integration-endpoints.get-person-info}")
    private String personInfoUrl;

    private final CommonIntegrationService commonIntegrationService;

    @Override
    public ResponseEntity<Object> getPersonInfo(UUID personId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = commonIntegrationService.buildHeaders();
        HttpEntity<Object> request = new HttpEntity<>(header);
        return restTemplate.exchange(
                personInfoUrl,
                HttpMethod.GET,
                request,
                Object.class,
                personId
        );
    }

}
