package ru.tsu.hits.kosterror.messenger.core.integration.filestorage;

import org.springframework.lang.NonNull;
import ru.tsu.hits.kosterror.messenger.core.dto.FileMetaDataDto;

import java.util.UUID;

/**
 * Интерфейс, предоставляющий методы для интеграционных запросов в {@code file-storage-service}.
 */
public interface FileStorageIntegrationService {

    /**
     * Получает метаинформацию о файле.
     *
     * @param fileId идентификатор файла.
     * @return метаинформация о файле.
     */
    FileMetaDataDto getFileMetaData(@NonNull UUID fileId);

}
