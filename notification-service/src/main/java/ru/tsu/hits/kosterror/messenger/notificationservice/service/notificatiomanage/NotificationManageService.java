package ru.tsu.hits.kosterror.messenger.notificationservice.service.notificatiomanage;

import ru.tsu.hits.kosterror.messenger.core.dto.NewNotificationDto;
import ru.tsu.hits.kosterror.messenger.core.dto.NumberDto;
import ru.tsu.hits.kosterror.messenger.notificationservice.dto.NotificationsStatusDto;

import java.util.UUID;

/**
 * Интерфейс, предоставляющий методы для управления уведомлениями.
 */
public interface NotificationManageService {

    /**
     * Создает уведомление.
     *
     * @param dto информация об уведомлении.
     */
    void createNotification(NewNotificationDto dto);

    /**
     * Изменяет статус уведомлений.
     *
     * @param dto      информация об уведомлениях и их статусе.
     * @param personId идентификатор пользователя, чьи это уведомления.
     * @return список непрочитанных уведомлений.
     */
    NumberDto setNotificationStatus(NotificationsStatusDto dto, UUID personId);

}
