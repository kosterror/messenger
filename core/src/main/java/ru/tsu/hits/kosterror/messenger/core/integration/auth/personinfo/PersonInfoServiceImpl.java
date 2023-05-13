package ru.tsu.hits.kosterror.messenger.core.integration.auth.personinfo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;
import ru.tsu.hits.kosterror.messenger.core.integration.common.CommonIntegrationService;

import java.util.UUID;

@RequiredArgsConstructor
public class PersonInfoServiceImpl implements PersonInfoService {

    private final CommonIntegrationService commonIntegrationService;
    @Value("${app.integration-endpoints.get-person-info}")
    private String personInfoUrl;

    @Override
    public PersonDto getPersonInfo(UUID personId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = commonIntegrationService.buildHeaders();
        HttpEntity<Object> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                personInfoUrl,
                HttpMethod.GET,
                request,
                PersonDto.class,
                personId
        ).getBody();
    }

}
