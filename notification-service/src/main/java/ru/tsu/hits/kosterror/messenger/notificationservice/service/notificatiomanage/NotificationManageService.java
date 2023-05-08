package ru.tsu.hits.kosterror.messenger.notificationservice.service.notificatiomanage;

import ru.tsu.hits.kosterror.messenger.core.dto.NumberDto;
import ru.tsu.hits.kosterror.messenger.notificationservice.dto.NotificationsStatusDto;

import java.util.UUID;

public interface NotificationManageService {

    NumberDto setNotificationStatus(NotificationsStatusDto dto, UUID personId);

}
