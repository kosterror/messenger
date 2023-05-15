package ru.tsu.hits.kosterror.messenger.authservice.controller.integration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.messenger.authservice.service.person.PersonService;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;
import ru.tsu.hits.kosterror.messenger.core.util.HeaderValues;

import java.util.UUID;

/**
 * Контроллер для интеграционных запросов.
 */
@RestController
@RequestMapping("/integration/users/")
@RequiredArgsConstructor
@Tag(name = "Интеграционные запросы")
public class IntegrationPersonController {

    private final PersonService personService;

    /**
     * Эндпоинт для получения информации о пользователе.
     *
     * @param personId идентификатор пользователя.
     * @return объект {@link PersonDto} с информацией о пользователе.
     */
    @GetMapping("/{personId}")
    @Operation(
            summary = "Получить информацию о пользователе.",
            security = @SecurityRequirement(name = HeaderValues.HEADER_API_KEY)
    )
    public PersonDto getPerson(@PathVariable UUID personId) {
        return personService.getPersonInfo(personId);
    }

}
