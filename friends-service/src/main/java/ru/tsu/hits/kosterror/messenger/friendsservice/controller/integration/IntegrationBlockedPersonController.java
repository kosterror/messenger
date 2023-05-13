package ru.tsu.hits.kosterror.messenger.friendsservice.controller.integration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.messenger.core.dto.BooleanDto;
import ru.tsu.hits.kosterror.messenger.core.dto.PairPersonIdDto;
import ru.tsu.hits.kosterror.messenger.core.util.HeaderValues;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.BlockedPerson;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.display.DisplayBlockedPersonService;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.synchronize.SynchronizeBlockedPersonsService;

import java.util.UUID;

@RestController
@RequestMapping("/integration/friends/blocked-persons")
@RequiredArgsConstructor
@Tag(name = "Интеграционные запросы")
public class IntegrationBlockedPersonController {

    private final DisplayBlockedPersonService displayBlockedPersonService;
    private final SynchronizeBlockedPersonsService synchronizeBlockedPersonsService;

    @PostMapping
    @Operation(
            summary = "Проверить нахождение в черном списке",
            security = @SecurityRequirement(name = HeaderValues.HEADER_API_KEY)
    )
    public BooleanDto checkBlockingPerson(@RequestBody PairPersonIdDto pairPersonId) {
        return displayBlockedPersonService.personIsBlocked(pairPersonId.getOwnerId(), pairPersonId.getMemberId());
    }

    /**
     * Эндпоинт для синхронизации ФИО внешнего пользователя во всех сущностях {@link BlockedPerson} с auth-service.
     *
     * @param blockedPersonId идентификатор внешнего пользователя.
     */
    @PatchMapping("/{blockedPersonId}")
    @Operation(
            summary = "Синхронизировать ФИО пользователя, среди заблокированных пользователей.",
            security = @SecurityRequirement(name = HeaderValues.HEADER_API_KEY)
    )
    public void synchronizeBlockedPersonFullName(@PathVariable UUID blockedPersonId) {
        synchronizeBlockedPersonsService.syncBlockedPersonIdFullName(blockedPersonId);
    }

}
