package ru.tsu.hits.kosterror.messenger.notificationservice.service.notifiocationinfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.core.dto.NumberDto;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingParamsResponse;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;
import ru.tsu.hits.kosterror.messenger.notificationservice.dto.NotificationDto;
import ru.tsu.hits.kosterror.messenger.notificationservice.dto.NotificationFilters;
import ru.tsu.hits.kosterror.messenger.notificationservice.entity.Notification;
import ru.tsu.hits.kosterror.messenger.notificationservice.entity.Notification_;
import ru.tsu.hits.kosterror.messenger.notificationservice.mapper.NotificationMapper;
import ru.tsu.hits.kosterror.messenger.notificationservice.repository.NotificationRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.tsu.hits.kosterror.messenger.notificationservice.service.specification.NotificationSpecification.*;

/**
 * Класс, реализующий метод {@link NotificationInfoService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationInfoServiceImpl implements NotificationInfoService {

    private final NotificationRepository repository;
    private final NotificationMapper mapper;

    @Override
    public NumberDto countUncheckedNotifications(UUID personId) {
        Integer countUncheckedNotifications = repository.countByPersonIdAndIsChecked(personId, false);
        return new NumberDto(countUncheckedNotifications);
    }

    @Override
    public PagingResponse<List<NotificationDto>> getNotifications(UUID personId,
                                                                  PagingFilteringRequest<NotificationFilters> request) {
        Specification<Notification> specification = buildSpecification(personId, request.getFilters());
        Page<Notification> pageNotification = repository.findAll(
                specification,
                PageRequest.of(request.getPaging().getPage(),
                        request.getPaging().getSize(),
                        Sort.by(Sort.Direction.DESC, Notification_.RECEIVED_DATE)
                )
        );

        return buildPagingResponse(pageNotification);
    }

    /**
     * Метод для построения спецификации для запроса в БД.
     *
     * @param personId идентификатор пользователя.
     * @param filters  параметры.
     * @return спецификация.
     */
    private Specification<Notification> buildSpecification(UUID personId, NotificationFilters filters) {
        Specification<Notification> specification = personId(personId);

        if (filters.getMessage() != null && !filters.getMessage().isEmpty() && !filters.getMessage().isBlank()) {
            specification = specification.and(messageContainsSubstring(filters.getMessage()));
        }

        if (filters.getNotificationTypes() != null && !filters.getNotificationTypes().isEmpty()) {
            specification = specification.and(typeIn(filters.getNotificationTypes()));
        }

        if (filters.getStartDate() != null) {
            specification = specification.and(receivedDateTimeAfter(filters.getStartDate()));
        }

        if (filters.getFinishDate() != null) {
            specification = specification.and(receivedDateTimeBefore(filters.getFinishDate()));
        }

        return specification;
    }

    /**
     * Метод для построения ответа в виде списка, обернутого в {@link PagingResponse}.
     *
     * @param notificationPage страница сущностей.
     * @return список {@link NotificationDto}, обернутый в {@link PagingResponse}.
     */
    private PagingResponse<List<NotificationDto>> buildPagingResponse(Page<Notification> notificationPage) {
        List<NotificationDto> notifications = notificationPage
                .getContent()
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());

        PagingParamsResponse pagingParams = new PagingParamsResponse(
                notificationPage.getTotalPages(),
                notificationPage.getTotalElements(),
                notificationPage.getNumber(),
                notificationPage.getSize()
        );

        return new PagingResponse<>(pagingParams, notifications);
    }

}
