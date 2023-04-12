package ru.tsu.hits.kosterror.messenger.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.UpdatePersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.request.PersonPageRequest;
import ru.tsu.hits.kosterror.messenger.authservice.service.account.AccountService;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;

import javax.validation.Valid;
import java.util.List;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtPersonDataExtractor.extractJwtPersonData;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Профиль")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @Operation(
            summary = "Получить информация о профиле",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public PersonDto getAccountInfo(Authentication authentication) throws NotFoundException {
        return service.getAccountInfo(extractJwtPersonData(authentication).getLogin());
    }

    @Operation(
            summary = "Изменить данные профиля",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping
    public PersonDto updateAccount(Authentication authentication,
                                   @RequestBody @Valid UpdatePersonDto dto
    ) throws NotFoundException {
        return service.updateAccount(extractJwtPersonData(authentication).getLogin(), dto);
    }

    @Operation(
            summary = "Получить список пользователей",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    public PagingResponse<List<PersonDto>> getPersons(@RequestBody @Valid PersonPageRequest personPageRequest) {
        return service.getPersons(personPageRequest);
    }

}
