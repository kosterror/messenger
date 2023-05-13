package ru.tsu.hits.kosterror.messenger.friendsservice.controller.integration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.messenger.core.util.HeaderValues;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.Friend;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.synchronize.SynchronizeFriendsService;

import java.util.UUID;

@RestController
@RequestMapping("/integration/friends/")
@RequiredArgsConstructor
@Tag(name = "Интеграционные запросы")
public class IntegrationFriendController {

    private final SynchronizeFriendsService synchronizeFriendsService;

    /**
     * Эндпоинт для синхронизации ФИО внешнего пользователя во всех сущностях {@link Friend} с auth-service.
     *
     * @param friendId идентификатор внешнего пользователя.
     */
    @PatchMapping("/{friendId}")
    @Operation(
            summary = "Синхронизировать ФИО внешнего пользователя",
            security = @SecurityRequirement(name = HeaderValues.HEADER_API_KEY)
    )
    public void synchronizeFriendFullName(@PathVariable UUID friendId) {
        synchronizeFriendsService.syncFriendFullName(friendId);
    }

}
