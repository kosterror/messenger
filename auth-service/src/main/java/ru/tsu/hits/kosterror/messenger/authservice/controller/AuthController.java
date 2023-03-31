package ru.tsu.hits.kosterror.messenger.authservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.RegisterPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.token.PairTokenDto;
import ru.tsu.hits.kosterror.messenger.authservice.service.auth.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Tag(name = "Авторизация, аутентификация, логаут")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/register")
    public PairTokenDto register(@RequestBody @Valid RegisterPersonDto dto) {
        return service.register(dto);
    }

}
