package ru.tsu.hits.kosterror.messenger.core.integration.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.tsu.hits.kosterror.messenger.core.dto.BooleanDto;
import ru.tsu.hits.kosterror.messenger.core.dto.PairPersonIdDto;
import ru.tsu.hits.kosterror.messenger.core.integration.common.CommonIntegrationService;

import java.util.UUID;

@RequiredArgsConstructor
public class FriendIntegrationServiceImpl implements FriendIntegrationService {

    private final CommonIntegrationService commonIntegrationService;

    @Value("${app.integration-endpoints.person-is-blocked}")
    private String personIsBlocked;

    @Override
    public BooleanDto checkPersonInfoIsBlocked(UUID ownerId, UUID memberId) {
        RestTemplate restTemplate = new RestTemplate();
        PairPersonIdDto idPair = new PairPersonIdDto(ownerId, memberId);
        HttpHeaders headers = commonIntegrationService.buildHeaders();
        HttpEntity<PairPersonIdDto> request = new HttpEntity<>(idPair, headers);
        ResponseEntity<BooleanDto> response = restTemplate
                .postForEntity(
                        personIsBlocked,
                        request,
                        BooleanDto.class
                );

        return response.getBody();
    }
}
