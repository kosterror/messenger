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

@RequiredArgsConstructor
public class FriendIntegrationServiceImpl implements FriendIntegrationService {

    private final RestTemplate restTemplate;
    private final CommonIntegrationService commonIntegrationService;

    @Value("${app.integration-endpoints.person-is-blocked}")
    private String personIsBlocked;

    @Value("${app.integration-endpoints.is-friends}")
    private String personsIfFriends;

    @Override
    public BooleanDto checkPersonInfoIsBlocked(PairPersonIdDto dto) {
        HttpHeaders headers = commonIntegrationService.buildHeaders();
        HttpEntity<PairPersonIdDto> request = new HttpEntity<>(dto, headers);
        ResponseEntity<BooleanDto> response = restTemplate
                .postForEntity(
                        personIsBlocked,
                        request,
                        BooleanDto.class
                );

        return response.getBody();
    }

    @Override
    public BooleanDto checkIsFriends(PairPersonIdDto dto) {
        HttpHeaders headers = commonIntegrationService.buildHeaders();
        HttpEntity<PairPersonIdDto> request = new HttpEntity<>(dto, headers);
        ResponseEntity<BooleanDto> response = restTemplate
                .postForEntity(
                        personsIfFriends,
                        request,
                        BooleanDto.class
                );

        return response.getBody();
    }
}
