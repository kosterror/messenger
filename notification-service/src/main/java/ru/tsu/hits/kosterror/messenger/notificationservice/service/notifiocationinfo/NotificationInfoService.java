package ru.tsu.hits.kosterror.messenger.notificationservice.service.notifiocationinfo;

import ru.tsu.hits.kosterror.messenger.core.dto.NumberDto;

import java.util.UUID;

public interface NotificationInfoService {

    NumberDto countUncheckedNotifications(UUID personId);

}
