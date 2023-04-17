package ru.tsu.hits.kosterror.messenger.authservice.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.UpdatePersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.request.PersonPageRequest;
import ru.tsu.hits.kosterror.messenger.authservice.service.person.PersonService;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;

import javax.validation.Valid;
import java.util.List;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor.extractPersonData;

/**
 * Контроллер с эндпоинтами для пользоватлея.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Профиль")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService service;

    /**
     * Эндпоинт для получения информации о своем профиле.
     *
     * @param authentication данные об аутентифицированном пользователе.
     * @return объект {@link PersonDto}.
     */
    @GetMapping
    @Operation(
            summary = "Просмотр информации о себе.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public PersonDto getMyPersonInfo(Authentication authentication) {
        return service.getMyPersonInfo(extractPersonData(authentication).getLogin());
    }

    /**
     * Эндпоинт для изменения профиля пользователя.
     *
     * @param authentication данные об аутентифицированном пользователе.
     * @param dto            новые данные о пользователе.
     * @return объект {@link PersonDto}.
     */
    @PutMapping
    @Operation(
            summary = "Изменения профиля.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public PersonDto updatePersonInfo(Authentication authentication,
                                      @RequestBody @Valid UpdatePersonDto dto
    ) {
        return service.updatePersonInfo(extractPersonData(authentication).getLogin(), dto);
    }

    /**
     * Эндпоинт для получения списка пользователей.
     *
     * @param personPageRequest информации о пагинации, фильтрации, сортировки.
     * @return список {@link PersonDto} с информацией о пагинации.
     */
    @PostMapping
    @Operation(
            summary = "Список пользователей.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public PagingResponse<List<PersonDto>> getPersons(@RequestBody @Valid PersonPageRequest personPageRequest) {
        return service.getPersons(personPageRequest);
    }

    /**
     * Эндпоинт для просмотра профиля пользователя.
     *
     * @param auth  данные об аутентифицированном пользователе.
     * @param login логин пользователя.
     * @return информация о пользователе.
     */
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
