package ru.tsu.hits.kosterror.messenger.friendsservice.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.FriendDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.manage.ManageFriendService;

import java.util.UUID;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor.extractId;

/**
 * Контроллер с методами для управления друзьями.
 */
@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@Tag(name = "Друзья")
public class ManageFriendController {

    private final ManageFriendService manageFriendService;

    @PostMapping("/add/{id}")
    @Operation(
            summary = "Добавить друга.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public FriendDto createFriend(Authentication auth,
                                  @PathVariable UUID id) {
        return manageFriendService.createFriend(extractId(auth), id);
    }

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
        manageFriendService.deleteFriend(JwtExtractor.extractId(auth), friendId);
    }

}
