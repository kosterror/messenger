package ru.tsu.hits.kosterror.messenger.filestorageservice.service;

import org.springframework.web.multipart.MultipartFile;
import ru.tsu.hits.kosterror.messenger.core.dto.FileMetaDataDto;

import java.util.UUID;

public interface FileStorageService {

    FileMetaDataDto uploadFile(UUID authorId, MultipartFile file);

}
