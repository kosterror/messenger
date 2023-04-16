package ru.tsu.hits.kosterror.messenger.authservice.controller.integration;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.service.person.PersonService;

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
    public PersonDto getPerson(@PathVariable UUID personId) {
        return personService.getPersonInfo(personId);
    }

}
