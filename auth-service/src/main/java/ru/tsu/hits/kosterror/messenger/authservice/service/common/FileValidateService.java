package ru.tsu.hits.kosterror.messenger.authservice.service.common;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Интерфейс предоставляющий методы для валидации файлов.
 */
public interface FileValidateService {

    /**
     * Проверяет существования файла в file-storage-service с authorId = personId.
     *
     * @param personId владелец файла.
     * @param avatarId идентификатор аватарки из file-storage-service.
     */
    void isValidAvatarId(@NotNull UUID personId, @Nullable UUID avatarId);

}
