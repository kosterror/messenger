package ru.tsu.hits.kosterror.messenger.notificationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.messenger.core.dto.NumberDto;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;
import ru.tsu.hits.kosterror.messenger.notificationservice.dto.NotificationDto;
import ru.tsu.hits.kosterror.messenger.notificationservice.dto.NotificationFilters;
import ru.tsu.hits.kosterror.messenger.notificationservice.service.notifiocationinfo.NotificationInfoService;

import java.util.List;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor.extractId;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationInfoController {

    private final NotificationInfoService service;

    @GetMapping("/unchecked")
    @Operation(
            summary = "Получить количество непрочитанных уведомлений.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public NumberDto countUncheckedNotifications(Authentication auth) {
        return service.countUncheckedNotifications(extractId(auth));
    }

    @PostMapping
    @Operation(
            summary = "Получить список уведомлений.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public List<PagingResponse<NotificationDto>> getNotifications(Authentication auth,
                                                                  @RequestBody PagingFilteringRequest<NotificationFilters> request) {
        return service.getNotifications(extractId(auth), request.getFilters());
    }

}
