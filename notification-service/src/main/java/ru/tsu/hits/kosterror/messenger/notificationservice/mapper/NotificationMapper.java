package ru.tsu.hits.kosterror.messenger.notificationservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.kosterror.messenger.core.dto.NewNotificationDto;
import ru.tsu.hits.kosterror.messenger.notificationservice.dto.NotificationDto;
import ru.tsu.hits.kosterror.messenger.notificationservice.entity.Notification;

/**
 * Интерфейс, на основе которого сгенерируется класс для маппинга {@link Notification} в связанные
 * с ним DTO и наоборот.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface NotificationMapper {

    NotificationDto entityToDto(Notification entity);

    Notification newDtoToEntity(NewNotificationDto newDto);

}
