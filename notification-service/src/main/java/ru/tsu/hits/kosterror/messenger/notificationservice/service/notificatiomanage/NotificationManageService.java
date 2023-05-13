package ru.tsu.hits.kosterror.messenger.notificationservice.service.notificatiomanage;

import ru.tsu.hits.kosterror.messenger.core.dto.NewNotificationDto;
import ru.tsu.hits.kosterror.messenger.core.dto.NumberDto;
import ru.tsu.hits.kosterror.messenger.notificationservice.dto.NotificationsStatusDto;

import java.util.UUID;

public interface NotificationManageService {

    void createNotification(NewNotificationDto dto);

    NumberDto setNotificationStatus(NotificationsStatusDto dto, UUID personId);

}
