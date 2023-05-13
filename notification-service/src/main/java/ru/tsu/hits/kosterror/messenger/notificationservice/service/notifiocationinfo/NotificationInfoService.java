package ru.tsu.hits.kosterror.messenger.notificationservice.service.notifiocationinfo;

import ru.tsu.hits.kosterror.messenger.core.dto.NumberDto;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;
import ru.tsu.hits.kosterror.messenger.notificationservice.dto.NotificationDto;
import ru.tsu.hits.kosterror.messenger.notificationservice.dto.NotificationFilters;

import java.util.List;
import java.util.UUID;

public interface NotificationInfoService {

    NumberDto countUncheckedNotifications(UUID personId);

    PagingResponse<List<NotificationDto>> getNotifications(UUID personId,
                                                           PagingFilteringRequest<NotificationFilters> request);

}
