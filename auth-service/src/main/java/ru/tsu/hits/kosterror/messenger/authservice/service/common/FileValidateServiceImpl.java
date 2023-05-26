package ru.tsu.hits.kosterror.messenger.authservice.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.tsu.hits.kosterror.messenger.core.dto.FileMetaDataDto;
import ru.tsu.hits.kosterror.messenger.core.exception.ForbiddenException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.integration.filestorage.FileStorageIntegrationService;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Класс, реализующий {@link FileValidateService}.
 */
@Service
@RequiredArgsConstructor
public class FileValidateServiceImpl implements FileValidateService {

    private final FileStorageIntegrationService fileStorageIntegrationService;

    @Override
    public void isValidAvatarId(@NotNull UUID personId, @Nullable UUID avatarId) {
        try {
            if (avatarId == null) {
                return;
            }
            FileMetaDataDto fileMetaData = fileStorageIntegrationService.getFileMetaData(avatarId);
            if (!fileMetaData.getAuthorId().equals(personId)) {
                throw new ForbiddenException("Файл для аватарки не принадлежит вам");
            }
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException(String.format("Файл с id = %s не найден", avatarId), exception);
        }
    }

}
