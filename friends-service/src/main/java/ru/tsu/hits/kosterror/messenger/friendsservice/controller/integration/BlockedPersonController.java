package ru.tsu.hits.kosterror.messenger.friendsservice.controller.integration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.messenger.core.dto.BooleanDto;
import ru.tsu.hits.kosterror.messenger.core.dto.PairPersonIdDto;
import ru.tsu.hits.kosterror.messenger.coresecurity.util.Constants;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.display.DisplayBlockedPersonService;

@RestController
@RequestMapping("/integration/friends/blocked-persons")
@RequiredArgsConstructor
@Tag(name = "Интеграционные запросы")
public class BlockedPersonController {

    private final DisplayBlockedPersonService displayBlockedPersonService;

    @PostMapping
    @Operation(
            summary = "Проверить нахождение в черном списке",
            security = @SecurityRequirement(name = Constants.HEADER_API_KEY)
    )
    public BooleanDto checkBlockingPerson(@RequestBody PairPersonIdDto pairPersonId) {
        return displayBlockedPersonService.personIsBlocked(pairPersonId.getOwnerId(), pairPersonId.getMemberId());
    }

}
