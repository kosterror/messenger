package ru.tsu.hits.kosterror.messenger.notificationservice.service.notificatiomanage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.messenger.core.dto.NumberDto;
import ru.tsu.hits.kosterror.messenger.core.exception.ForbiddenException;
import ru.tsu.hits.kosterror.messenger.notificationservice.dto.NotificationsStatusDto;
import ru.tsu.hits.kosterror.messenger.notificationservice.entity.Notification;
import ru.tsu.hits.kosterror.messenger.notificationservice.repository.NotificationRepository;
import ru.tsu.hits.kosterror.messenger.notificationservice.service.notifiocationinfo.NotificationInfoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationManageServiceImpl implements NotificationManageService {

    private final NotificationRepository repository;
    private final NotificationInfoService notificationInfoService;

    @Override
    @Transactional
    public NumberDto setNotificationStatus(NotificationsStatusDto dto, UUID personId) {
        List<Notification> notifications = repository.findAllById(dto.getNotificationIds());

        if (!areNotificationBelongToPerson(notifications, personId)) {
            throw new ForbiddenException("Какое-то уведомление принадлежит не вам");
        }

        if (Objects.equals(Boolean.TRUE, dto.getIsChecked())) {
            setChecked(notifications);
        } else {
            setUnchecked(notifications);
        }

        repository.saveAll(notifications);
        return notificationInfoService.countUncheckedNotifications(personId);
    }

    private void setChecked(List<Notification> notifications) {
        LocalDateTime dateTime = LocalDateTime.now();

        notifications.forEach(notification -> {
            notification.setIsChecked(true);
            notification.setCheckedDate(dateTime);
        });
    }

    private void setUnchecked(List<Notification> notifications) {
        notifications.forEach(notification -> {
            notification.setIsChecked(false);
            notification.setCheckedDate(null);
        });
    }

    private boolean areNotificationBelongToPerson(List<Notification> notifications, UUID personId) {
        long count = notifications
                .stream()
                .filter(n -> n.getPersonId().equals(personId))
                .count();

        return count == notifications.size();
    }

}
