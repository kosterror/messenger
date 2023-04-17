package ru.tsu.hits.kosterror.messenger.authservice.service.integration.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.coresecurity.util.Constants;

import java.util.List;

/**
 * Класс, реализующий интерфейс {@link CommonIntegrationService}.
 */
@Service
public class CommonIntegrationServiceImpl implements CommonIntegrationService {

    @Value("${app.security.integration.api-key}")
    private String apiKey;

    @Override
    public HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set(Constants.HEADER_API_KEY, apiKey);

        return headers;
    }

}
