package ru.tsu.hits.kosterror.messenger.friendsservice.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.manage.BlockedPersonManageService;

import java.util.UUID;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtPersonDataExtractor.extractJwtPersonData;

@RestController
@RequestMapping("/api/friends/blocked-persons")
@RequiredArgsConstructor
@Tag(name = "Взаимодействие с заблокированными пользователями")
public class BlockedPersonManageController {

    private final BlockedPersonManageService blockedPersonManageService;

    @DeleteMapping("/{blockedPersonId}")
    @Operation(
            summary = "Удалить пользователя из черного списка.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void deleteFriend(Authentication auth,
                             @PathVariable UUID blockedPersonId
    ) {
        blockedPersonManageService.deleteBlockedPerson(extractJwtPersonData(auth).getId(), blockedPersonId);
    }

}
