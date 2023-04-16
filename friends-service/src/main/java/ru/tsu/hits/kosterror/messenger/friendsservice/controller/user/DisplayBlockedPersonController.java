package ru.tsu.hits.kosterror.messenger.friendsservice.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.messenger.core.dto.BooleanDto;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;
import ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.BlockedPersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.request.BlockedPersonBasicFilters;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.display.DisplayBlockedPersonService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

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
     * Получение информации о заблокированном пользователе.
     *
     * @param auth            аутентификационные данные пользователя.
     * @param blockedPersonId идентификатор пользователя, данные, о котором нужно получить.
     * @return данные о профиле заблокированном пользователе.
     */
    @GetMapping("/{blockedPersonId}")
    @Operation(
            summary = "Просмотр профиля пользователя из черного списка.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public BlockedPersonDto getBlockedPerson(Authentication auth,
                                             @PathVariable UUID blockedPersonId) {
        return service.getBlockedPerson(JwtExtractor.extractId(auth), blockedPersonId);
    }

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
        return service.getBlockedPersons(JwtExtractor.extractId(auth), pagingFilteringRequest);
    }

    /**
     * Эндпоинт для проверки нахождения в черном списке целевого пользователя у внешнего.
     *
     * @param auth информация о целевом пользователе.
     * @param id   идентификатор внешнего пользователя.
     * @return находится ли целевой пользователь у внешнего в черном списке.
     */
    @GetMapping("/is-blocked/{id}")
    @Operation(
            summary = "Проверка нахождения в черном списке"
    )
    public BooleanDto personIsBlocked(Authentication auth,
                                      @PathVariable @Valid UUID id) {
        return service.personIsBlocked(id, JwtExtractor.extractId(auth));
    }

}
