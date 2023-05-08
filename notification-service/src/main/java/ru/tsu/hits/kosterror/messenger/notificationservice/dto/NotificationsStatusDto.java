package ru.tsu.hits.kosterror.messenger.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationsStatusDto {

    private List<UUID> notificationIds;

    private Boolean isChecked;

}
