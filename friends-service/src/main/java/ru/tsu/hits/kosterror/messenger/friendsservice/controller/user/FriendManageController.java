package ru.tsu.hits.kosterror.messenger.friendsservice.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.manage.FriendManageService;

import java.util.UUID;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtPersonDataExtractor.extractJwtPersonData;

/**
 * Контроллер с методами для управления друзьями.
 */
@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendManageController {

    private final FriendManageService friendManageService;

    /**
     * Эндпоинт для удаления друга.
     *
     * @param auth     информация об аутентифицированном пользователе.
     * @param friendId идентификатор друга.
     */
    @DeleteMapping("/{friendId}")
    @Operation(
            summary = "Удалить друга.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void deleteFriend(Authentication auth,
                             @PathVariable UUID friendId) {
        friendManageService.deleteFriend(extractJwtPersonData(auth).getId(), friendId);
    }

}
