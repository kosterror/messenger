package ru.tsu.hits.kosterror.messenger.filestorageservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.messenger.core.dto.FileMetaDataDto;
import ru.tsu.hits.kosterror.messenger.core.util.HeaderValues;
import ru.tsu.hits.kosterror.messenger.filestorageservice.service.filestorage.FileStorageService;

import java.util.UUID;

/**
 * Контроллер для интеграционных запросов.
 */
@RestController
@RequestMapping("/integration/file-storage")
@RequiredArgsConstructor
@Tag(name = "Интеграционные запросы")
public class IntegrationController {

    private final FileStorageService fileStorageService;

    /**
     * Метод для получения метаинформации о файле.
     *
     * @param fileId идентификатор файла.
     * @return метаинформация о файле.
     */
    @GetMapping("/{fileId}")
    @Operation(
            summary = "Получить метаинформацию о пользователе.",
            security = @SecurityRequirement(name = HeaderValues.HEADER_API_KEY)
    )
    public FileMetaDataDto getFileMetaData(@PathVariable UUID fileId) {
        return fileStorageService.getFileMetaData(fileId);
    }

}
