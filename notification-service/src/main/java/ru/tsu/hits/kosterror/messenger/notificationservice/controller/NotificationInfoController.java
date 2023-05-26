package ru.tsu.hits.kosterror.messenger.notificationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.messenger.core.dto.NumberDto;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;
import ru.tsu.hits.kosterror.messenger.notificationservice.dto.NotificationDto;
import ru.tsu.hits.kosterror.messenger.notificationservice.dto.NotificationFilters;
import ru.tsu.hits.kosterror.messenger.notificationservice.service.notifiocationinfo.NotificationInfoService;

import javax.validation.Valid;
import java.util.List;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor.extractId;

/**
 * Контроллер для отображения уведомлений.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Информация об уведомлениях")
public class NotificationInfoController {

    private final NotificationInfoService service;

    /**
     * Получить количество непрочитанных уведомлений.
     *
     * @param auth информация о пользователе.
     * @return объект с числом непрочитанных уведомлений.
     */
    @GetMapping("/unchecked")
    @Operation(
            summary = "Получить количество непрочитанных уведомлений.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public NumberDto countUncheckedNotifications(Authentication auth) {
        return service.countUncheckedNotifications(extractId(auth));
    }

    /**
     * Получить список уведомлений.
     *
     * @param auth    информация о пользователе.
     * @param request запрос с пагинацией и фильтрацией.
     * @return список уведомлений.
     */
    @PostMapping
    @Operation(
            summary = "Получить список уведомлений.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public PagingResponse<List<NotificationDto>> getNotifications(Authentication auth,
                                                                  @RequestBody @Valid
                                                                  PagingFilteringRequest<NotificationFilters> request) {
        return service.getNotifications(extractId(auth), request);
    }

}
