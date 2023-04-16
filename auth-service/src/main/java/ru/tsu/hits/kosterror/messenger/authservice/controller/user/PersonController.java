package ru.tsu.hits.kosterror.messenger.authservice.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.UpdatePersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.request.PersonPageRequest;
import ru.tsu.hits.kosterror.messenger.authservice.service.account.PersonService;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;

import javax.validation.Valid;
import java.util.List;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor.extractPersonData;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Профиль")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService service;

    @GetMapping
    @Operation(
            summary = "Просмотр информации о себе.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public PersonDto getMyPersonInfo(Authentication authentication) throws NotFoundException {
        return service.getMyPersonInfo(extractPersonData(authentication).getLogin());
    }

    @PutMapping
    @Operation(
            summary = "Изменения профиля.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public PersonDto updatePersonInfo(Authentication authentication,
                                      @RequestBody @Valid UpdatePersonDto dto
    ) throws NotFoundException {
        return service.updatePersonInfo(extractPersonData(authentication).getLogin(), dto);
    }

    @PostMapping
    @Operation(
            summary = "Список пользователей.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public PagingResponse<List<PersonDto>> getPersons(@RequestBody @Valid PersonPageRequest personPageRequest) {
        return service.getPersons(personPageRequest);
    }

    @GetMapping("/{login}")
    @Operation(
            summary = "Просмотр профиля пользователя.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public PersonDto getPersonInfo(Authentication auth,
                                   @PathVariable String login) {
        return service.getPersonInfo(extractPersonData(auth).getLogin(), login);
    }

}
