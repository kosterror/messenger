package ru.tsu.hits.kosterror.messenger.notificationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.messenger.core.dto.NumberDto;
import ru.tsu.hits.kosterror.messenger.notificationservice.service.notifiocationinfo.NotificationInfoService;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor.extractId;

@RestController
@RequestMapping("/api/notification/")
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

}
