package ru.tsu.hits.kosterror.messenger.chatservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Chat;

/**
 * Интерфейс, на основе которого сгенерируется класс для маппинга сущности чата в связанные с ним dto и наоборот.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ChatMapper {

    ChatDto entityToDto(Chat entity);

}
