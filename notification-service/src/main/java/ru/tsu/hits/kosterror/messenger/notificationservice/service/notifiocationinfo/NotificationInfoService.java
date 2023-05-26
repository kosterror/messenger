package ru.tsu.hits.kosterror.messenger.notificationservice.service.notifiocationinfo;

import ru.tsu.hits.kosterror.messenger.core.dto.NumberDto;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;
import ru.tsu.hits.kosterror.messenger.notificationservice.dto.NotificationDto;
import ru.tsu.hits.kosterror.messenger.notificationservice.dto.NotificationFilters;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс для отображения уведомлений.
 */
public interface NotificationInfoService {

    /**
     * Количество непрочитанных уведомлений.
     *
     * @param personId идентификатор пользователя.
     * @return количество непрочитанных уведомлений.
     */
    NumberDto countUncheckedNotifications(UUID personId);

    /**
     * Получить уведомления пользователя.
     *
     * @param personId идентификатор пользователя.
     * @param request  dto с пагинацией и фильтрацией.
     * @return список найденных уведомлений.
     */
    PagingResponse<List<NotificationDto>> getNotifications(UUID personId,
                                                           PagingFilteringRequest<NotificationFilters> request);

}
