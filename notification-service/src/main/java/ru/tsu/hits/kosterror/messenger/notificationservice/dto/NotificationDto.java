package ru.tsu.hits.kosterror.messenger.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.messenger.core.enumeration.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private UUID id;
    private NotificationType type;
    private String message;
    private Boolean isChecked;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime receivedDate;
}
