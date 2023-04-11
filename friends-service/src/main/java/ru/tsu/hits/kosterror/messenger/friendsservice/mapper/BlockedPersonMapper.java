package ru.tsu.hits.kosterror.messenger.friendsservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.BlockedPersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.BlockedPerson;

/**
 * Интерфейс для конвертации сущности {@link BlockedPerson} в связанные с ним объекты и из них.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BlockedPersonMapper {

    /**
     * Метод для конвертации сущности в DTO.
     *
     * @param entity сущность.
     * @return DTO.
     */
    BlockedPersonDto entityToDto(BlockedPerson entity);

}
