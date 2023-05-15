package ru.tsu.hits.kosterror.messenger.core.integration.filestorage;

import org.springframework.lang.NonNull;
import ru.tsu.hits.kosterror.messenger.core.dto.FileMetaDataDto;

import java.util.UUID;

public interface FileStorageIntegrationService {

    FileMetaDataDto getFileMetaData(@NonNull UUID fileId);

}
