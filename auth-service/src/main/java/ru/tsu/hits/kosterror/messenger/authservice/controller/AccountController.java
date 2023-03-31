package ru.tsu.hits.kosterror.messenger.authservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.authservice.service.account.AccountService;

import java.security.Principal;

@RestController
@RequestMapping("/api/account")
@Tag(name = "Профиль")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @GetMapping
    public PersonDto getAccountInfo(Principal principal) throws NotFoundException {
        return service.getAccountInfo(principal.getName());
    }

}
