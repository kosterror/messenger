package ru.tsu.hits.kosterror.messenger.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.messenger.core.enumeration.NotificationType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO для параметров фильтрации уведомлений.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationFilters {
    List<NotificationType> notificationTypes;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private String message;
}
