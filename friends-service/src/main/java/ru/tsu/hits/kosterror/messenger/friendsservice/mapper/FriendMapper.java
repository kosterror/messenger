package ru.tsu.hits.kosterror.messenger.friendsservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.FriendDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.Friend;

/**
 * Интерфейс для конвертации сущности {@link Friend} в связанные с ним объекты и из них.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface FriendMapper {

    /**
     * Метод для конвертации сущности в DTO.
     *
     * @param entity сущность.
     * @return DTO.
     */
    FriendDto entityToDto(Friend entity);

}
