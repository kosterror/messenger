package ru.tsu.hits.kosterror.messenger.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.messenger.core.enumeration.NotificationType;

import java.util.UUID;

/**
 * DTO с информацией об отправляемом уведомлении.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewNotificationDto {
    private UUID personId;
    private NotificationType type;
    private String message;
}
