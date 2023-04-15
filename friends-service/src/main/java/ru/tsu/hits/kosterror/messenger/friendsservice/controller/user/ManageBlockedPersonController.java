package ru.tsu.hits.kosterror.messenger.friendsservice.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.BlockedPersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.CreateBlockedPersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.manage.ManageBlockedPersonService;

import javax.validation.Valid;
import java.util.UUID;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtPersonDataExtractor.extractJwtPersonData;

/**
 * Контроллер с эндпоинтами для управления пользователями из черного списка.
 */
@RestController
@RequestMapping("/api/friends/blocked-persons")
@RequiredArgsConstructor
@Tag(name = "Взаимодействие с заблокированными пользователями")
public class ManageBlockedPersonController {

    private final ManageBlockedPersonService manageBlockedPersonService;

    /**
     * Метод для добавления пользователя в чёрный список.
     *
     * @param auth информация об авторизованном пользователе.
     * @param dto  информация о пользователе, которого нужно добавить в чёрный список.
     * @return сохраненная информация о заблокированном пользователе.
     */
    @PostMapping("/create")
    @Operation(
            summary = "Добавить пользователя в чёрный список.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public BlockedPersonDto createBlockedPerson(Authentication auth,
                                                @RequestBody @Valid CreateBlockedPersonDto dto
    ) {
        return manageBlockedPersonService.createBlockedPerson(extractJwtPersonData(auth).getId(), dto);
    }

    /**
     * Метод для удаления пользователя из черного списка.
     *
     * @param auth            информация об авторизованном пользователе.
     * @param blockedPersonId идентификатор пользователя.
     */
    @DeleteMapping("/{blockedPersonId}")
    @Operation(
            summary = "Удалить пользователя из черного списка.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void deleteBlockedPerson(Authentication auth,
                                    @PathVariable UUID blockedPersonId
    ) {
        manageBlockedPersonService.deleteBlockedPerson(extractJwtPersonData(auth).getId(), blockedPersonId);
    }

}
