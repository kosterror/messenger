package ru.tsu.hits.kosterror.messenger.friendsservice.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.CreateFriendDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.FriendDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.manage.ManageFriendService;

import javax.validation.Valid;
import java.util.UUID;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtPersonDataExtractor.extractJwtPersonData;

/**
 * Контроллер с методами для управления друзьями.
 */
@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@Tag(name = "Взаимодействие с друзьями")
public class ManageFriendController {

    private final ManageFriendService manageFriendService;

    /**
     * Эндпоинт для добавления друга.
     *
     * @param auth информация об аутентифицированном пользователе.
     * @param dto  информация о новом друге.
     * @return сохраненная информация о новом друге.
     */
    @PostMapping("/create")
    @Operation(
            summary = "Добавить друга.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public FriendDto createFriend(Authentication auth,
                                  @RequestBody @Valid CreateFriendDto dto) {
        return manageFriendService.createFriend(extractJwtPersonData(auth), dto);
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
        manageFriendService.deleteFriend(extractJwtPersonData(auth).getId(), friendId);
    }

}
