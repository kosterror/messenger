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
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.BlockedPersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.request.BlockedPersonBasicFilters;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.display.DisplayBlockedPersonService;

import java.util.List;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor.extractPersonData;

/**
 * Контроллер для черного списка.
 */
@RestController
@RequestMapping("/api/friends/blocked-persons")
@RequiredArgsConstructor
@Tag(name = "Отображение заблокированных пользователей")
public class DisplayBlockedPersonController {

    private final DisplayBlockedPersonService service;

    /**
     * Эндпоинт для получения списка заблокированных пользователей текущего пользователя.
     *
     * @param auth                   информация о текущем пользователе.
     * @param pagingFilteringRequest параметры пагинации и фильтрации.
     * @return список заблокированных пользователей с информацией о пагинации.
     */
    @PostMapping
    @Operation(
            summary = "Получить заблокированных пользователей.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public PagingResponse<List<BlockedPersonDto>> getBlockedPersons(Authentication auth,
                                                                    @RequestBody
                                                                    PagingFilteringRequest<BlockedPersonBasicFilters>
                                                                            pagingFilteringRequest
    ) {
        return service.getBlockedPersons(extractPersonData(auth).getId(), pagingFilteringRequest);
    }

}
