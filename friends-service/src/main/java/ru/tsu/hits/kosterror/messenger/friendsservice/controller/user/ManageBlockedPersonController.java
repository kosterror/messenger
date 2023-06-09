package ru.tsu.hits.kosterror.messenger.friendsservice.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.BlockedPersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.manage.ManageBlockedPersonService;

import java.util.UUID;

/**
 * Контроллер с эндпоинтами для управления пользователями из черного списка.
 */
@RestController
@RequestMapping("/api/friends/blocked-persons")
@RequiredArgsConstructor
@Tag(name = "Черный список")
public class ManageBlockedPersonController {

    private final ManageBlockedPersonService manageBlockedPersonService;

    @PostMapping("/{id}")
    @Operation(
            summary = "Добавить пользователя в чёрный список.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public BlockedPersonDto createBlockedPerson(Authentication auth,
                                                @PathVariable UUID id
    ) {
        return manageBlockedPersonService.createBlockedPerson(JwtExtractor.extractId(auth), id);
    }

    /**
     * Метод для удаления пользователя из черного списка.
     *
     * @param auth            информация об авторизованном пользователе.
     * @param blockedPersonId идентификатор пользователя.
     */
    @DeleteMapping("/{blockedPersonId}")
    @Operation(
            summary = "Удалить пользователя из черного списка.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void deleteBlockedPerson(Authentication auth,
                                    @PathVariable UUID blockedPersonId
    ) {
        manageBlockedPersonService.deleteBlockedPerson(JwtExtractor.extractId(auth), blockedPersonId);
    }

}
