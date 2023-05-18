package ru.tsu.hits.kosterror.messenger.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Класс, который представляет собой метаинформацию о файле.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Объект для хранения метаинформации файла из file-storage-service.")
public class FileMetaDataDto {

    @Schema(description = "Идентификатор файла.")
    private UUID fileId;

    @Schema(description = "Название файла. Остается таким же, каким было при загрузке.",
            example = "Файл с таким названием.txt")
    private String name;

    @Schema(description = "Размер файла в байтах.", example = "124512")
    private Long sizeInBytes;

    @Schema(description = "Идентификатор пользователя, который загрузил этот файл.")
    private UUID authorId;

    @Schema(description = "Дата и время загрузки файла.")
    private LocalDateTime uploadDateTime;

}
