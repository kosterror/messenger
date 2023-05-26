package ru.tsu.hits.kosterror.messenger.filestorageservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.kosterror.messenger.core.dto.FileMetaDataDto;
import ru.tsu.hits.kosterror.messenger.filestorageservice.entity.FileMetaData;

/**
 * Интерфейс, на основе которого создастся класс для маппинга сущности
 * {@link FileMetaData} в связанные с ней DTO.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface FileMetaDataMapper {

    @Mapping(target = "fileId", source = "entity.id")
    FileMetaDataDto entityToDto(FileMetaData entity);

}
