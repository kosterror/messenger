package ru.tsu.hits.kosterror.messenger.filestorageservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.tsu.hits.kosterror.messenger.core.dto.FileMetaDataDto;
import ru.tsu.hits.kosterror.messenger.core.util.HeaderValues;
import ru.tsu.hits.kosterror.messenger.filestorageservice.service.filestorage.FileStorageService;

import java.util.UUID;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor.extractId;

/**
 * Контроллер для пользовательских запросов.
 */
@RestController
@RequestMapping("/api/file-storage")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Пользовательские запросы")
public class FileStorageController {

    private final FileStorageService fileStorageService;

    /**
     * Загружает файл в S3.
     *
     * @param auth информация о пользователе.
     * @param file загружаемый файл.
     * @return метаинформация о сохраненном файле.
     */
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Загрузить файл в хранилище.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public FileMetaDataDto uploadFile(Authentication auth,
                                      @RequestParam("file") MultipartFile file) {
        return fileStorageService.uploadFile(extractId(auth), file);
    }

    /**
     * Выгружает файл по fileId.
     *
     * @param fileId идентификатор файла.
     * @return файл с заданным fileId.
     */
    @GetMapping(value = "/download/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(
            summary = "Скачать файл.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<byte[]> downloadFile(@PathVariable @NonNull UUID fileId) {
        Pair<String, byte[]> fileAndFilename = fileStorageService.downloadFileAndFilename(fileId);

        return ResponseEntity
                .ok()
                .header(
                        HeaderValues.HEADER_CONTENT_DISPOSITION,
                        String.format("filename=%s", fileAndFilename.getFirst())
                )
                .body(fileAndFilename.getSecond());
    }

    /**
     * Метод для получения метаинформации о файле.
     *
     * @param fileId идентификатор файла.
     * @return метаинформация о файле.
     */
    @GetMapping(value = "/{fileId}")
    @Operation(
            summary = "Получить метаинформацию о файле.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public FileMetaDataDto getFileMetaDataDto(@PathVariable @NonNull UUID fileId) {
        return fileStorageService.getFileMetaData(fileId);
    }

}
