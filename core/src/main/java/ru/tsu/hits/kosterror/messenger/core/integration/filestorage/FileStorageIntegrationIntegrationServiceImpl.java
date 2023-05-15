package ru.tsu.hits.kosterror.messenger.core.integration.filestorage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;
import ru.tsu.hits.kosterror.messenger.core.dto.FileMetaDataDto;
import ru.tsu.hits.kosterror.messenger.core.integration.common.CommonIntegrationService;

import java.util.UUID;

@RequiredArgsConstructor
public class FileStorageIntegrationIntegrationServiceImpl implements FileStorageIntegrationService {

    private final CommonIntegrationService commonIntegrationService;
    @Value("${app.integration-endpoints.get-file-meta-data}")
    private String getFileMetaDataUrl;

    @Override
    public FileMetaDataDto getFileMetaData(@NonNull UUID fileId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = commonIntegrationService.buildHeaders();
        HttpEntity<Object> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                getFileMetaDataUrl,
                HttpMethod.GET,
                request,
                FileMetaDataDto.class,
                fileId
        ).getBody();

    }

}
