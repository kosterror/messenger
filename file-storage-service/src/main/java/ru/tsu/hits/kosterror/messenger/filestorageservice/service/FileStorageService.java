package ru.tsu.hits.kosterror.messenger.filestorageservice.service;

import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;
import ru.tsu.hits.kosterror.messenger.core.dto.FileMetaDataDto;

import java.util.UUID;

public interface FileStorageService {

    FileMetaDataDto uploadFile(@NonNull UUID authorId, @NonNull MultipartFile file);

    FileMetaDataDto getFileMetaData(@NonNull UUID fileId);

}
