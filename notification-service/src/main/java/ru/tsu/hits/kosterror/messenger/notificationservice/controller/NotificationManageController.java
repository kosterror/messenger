package ru.tsu.hits.kosterror.messenger.notificationservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.messenger.core.dto.NumberDto;
import ru.tsu.hits.kosterror.messenger.notificationservice.dto.NotificationsStatusDto;
import ru.tsu.hits.kosterror.messenger.notificationservice.service.notificatiomanage.NotificationManageService;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor.extractId;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationManageController {

    private final NotificationManageService notificationManageService;

    @PostMapping("/set-status")
    @Operation(
            summary = "Пометка уведомлений прочитанными/не прочитанными.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public NumberDto setNotificationStatus(@RequestBody NotificationsStatusDto dto, Authentication auth) {
        return notificationManageService.setNotificationStatus(dto, extractId(auth));
    }

}
