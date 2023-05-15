package ru.tsu.hits.kosterror.messenger.filestorageservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.tsu.hits.kosterror.messenger.core.dto.FileMetaDataDto;
import ru.tsu.hits.kosterror.messenger.filestorageservice.service.FileStorageService;

import java.util.UUID;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor.extractId;

@RestController
@RequestMapping("/api/file-storage")
@RequiredArgsConstructor
@Slf4j
public class FileStorageController {

    private final FileStorageService fileStorageService;

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

    @GetMapping("/{fileId}")
    @Operation(
            summary = "Получить метаинформацию о файле.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public FileMetaDataDto getFileMetaDataDto(@PathVariable @NonNull UUID fileId) {
        return fileStorageService.getFileMetaData(fileId);
    }

}
