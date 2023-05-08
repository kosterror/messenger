package ru.tsu.hits.kosterror.messenger.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.messenger.notificationservice.enumeration.NotificationType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationFilters {
    List<NotificationType> notificationTypes;
    private LocalDateTime startDate;
    private LocalDate finishDate;
    private String message;
}
