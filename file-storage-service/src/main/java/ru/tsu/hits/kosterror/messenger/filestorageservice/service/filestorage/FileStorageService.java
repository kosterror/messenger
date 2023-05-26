package ru.tsu.hits.kosterror.messenger.filestorageservice.service.filestorage;

import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;
import ru.tsu.hits.kosterror.messenger.core.dto.FileMetaDataDto;

import java.util.UUID;

/**
 * Интерфейс, предоставляющий методы для работы с файловым хранилищем.
 */
public interface FileStorageService {

    /**
     * Загружает файл в файловое хранилище.
     *
     * @param authorId идентификатор владельца файла.
     * @param file     сам файл.
     * @return метаинформация о сохраненном фалйе.
     */
    FileMetaDataDto uploadFile(@NonNull UUID authorId, @NonNull MultipartFile file);

    /**
     * Выгружает файл по его идентификатору.
     *
     * @param fileId идентификатор файла.
     * @return пара в виде названия файла и массива байт.
     */
    Pair<String, byte[]> downloadFileAndFilename(@NonNull UUID fileId);

    /**
     * Получает метаинформацию о конкретном файле по идентификатору.
     *
     * @param fileId идентификатор файла.
     * @return метаинформацию о полученном файле.
     */
    FileMetaDataDto getFileMetaData(@NonNull UUID fileId);

}
