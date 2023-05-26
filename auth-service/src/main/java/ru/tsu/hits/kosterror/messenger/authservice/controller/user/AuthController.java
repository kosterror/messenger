package ru.tsu.hits.kosterror.messenger.authservice.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonCredentialsDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.RegisterPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.token.FullPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.service.auth.AuthService;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;

import javax.validation.Valid;

import static ru.tsu.hits.kosterror.messenger.authservice.util.constant.Constants.HEADER_ACCESS_TOKEN;

/**
 * Контроллер с эндпоинтами для аутентификации и авторизации.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Авторизация, аутентификация, логаут")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    /**
     * Эндпоинт для регистрации.
     *
     * @param dto данные о пользователе.
     * @return в теле ответа объект {@link PersonDto} и access токен в хэдере.
     */
    @PostMapping("/register")
    @Operation(summary = "Зарегистрироваться", description = "ID аватарки можно указать после регистрации." +
            "Так как в file-storage-service нужно быть аутентифицированным, чтобы что-то загрузить.")
    public ResponseEntity<PersonDto> register(@RequestBody @Valid RegisterPersonDto dto) {
        FullPersonDto fullPersonDto = service.register(dto);

        PersonDto personDto = fullPersonDto.getPersonDto();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HEADER_ACCESS_TOKEN, fullPersonDto.getAccessToken());

        return new ResponseEntity<>(personDto, responseHeaders, HttpStatus.OK);
    }

    /**
     * Эндпоинт для аутентификации.
     *
     * @param dto данные для аутентификации.
     * @return в теле ответа объект {@link PersonDto} и access токен в хэдере.
     */
    @PostMapping("/login")
    @Operation(summary = "Аутентифицироваться")
    public ResponseEntity<PersonDto> login(@RequestBody @Valid PersonCredentialsDto dto) {
        FullPersonDto fullPersonDto = service.login(dto);

        PersonDto personDto = fullPersonDto.getPersonDto();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HEADER_ACCESS_TOKEN, fullPersonDto.getAccessToken());

        return new ResponseEntity<>(personDto, responseHeaders, HttpStatus.OK);
    }

}
