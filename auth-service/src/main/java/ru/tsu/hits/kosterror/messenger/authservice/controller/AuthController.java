package ru.tsu.hits.kosterror.messenger.authservice.controller;

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
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.RegisterPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.token.FullPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.service.auth.AuthService;
import ru.tsu.hits.kosterror.messenger.authservice.util.constant.HeaderKeys;
import ru.tsu.hits.kosterror.messenger.core.exception.UnauthorizedException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Tag(name = "Авторизация, аутентификация, логаут")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    @Operation(summary = "Зарегистрироваться")
    public ResponseEntity<PersonDto> register(@RequestBody @Valid RegisterPersonDto dto) {
        FullPersonDto fullPersonDto = service.register(dto);

        PersonDto personDto = fullPersonDto.getPersonDto();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HeaderKeys.ACCESS_TOKEN, fullPersonDto.getAccessToken());

        return new ResponseEntity<>(personDto, responseHeaders, HttpStatus.OK);
    }

    @PostMapping("/login")
    @Operation(summary = "Аутентифицироваться")
    public ResponseEntity<PersonDto> login(@RequestBody PersonCredentialsDto dto) throws UnauthorizedException {
        FullPersonDto fullPersonDto = service.login(dto);

        PersonDto personDto = fullPersonDto.getPersonDto();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HeaderKeys.ACCESS_TOKEN, fullPersonDto.getAccessToken());

        return new ResponseEntity<>(personDto, responseHeaders, HttpStatus.OK);
    }

}
