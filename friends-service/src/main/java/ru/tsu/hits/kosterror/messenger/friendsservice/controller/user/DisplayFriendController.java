package ru.tsu.hits.kosterror.messenger.friendsservice.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;
import ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.FriendDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.request.FriendBasicFilters;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.display.DisplayFriendService;

import java.util.List;

/**
 * Контроллер для отображения друзей.
 */
@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@Tag(name = "Отображение друзей")
public class DisplayFriendController {

    private final DisplayFriendService service;

    /**
     * Эндпоинт для получения списка друзей текущего пользователя.
     *
     * @param auth                   информация о текущем пользователе.
     * @param pagingFilteringRequest параметры пагинации и фильтрации.
     * @return список друзей с информацией о пагинации.
     */
    @PostMapping
    @Operation(
            summary = "Получить друзей.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public PagingResponse<List<FriendDto>> getFriends(Authentication auth,
                                                      @RequestBody
                                                      PagingFilteringRequest<FriendBasicFilters> pagingFilteringRequest
    ) {
        return service.getFriends(JwtExtractor.extractId(auth), pagingFilteringRequest);
    }

}
