package ru.tsu.hits.kosterror.messenger.authservice.service.auth;

import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonCredentialsDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.RegisterPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.token.FullPersonDto;
import ru.tsu.hits.kosterror.messenger.core.exception.UnauthorizedException;

/**
 * Сервис для регистрации и аутентификации пользователя.
 */
public interface AuthService {

    /**
     * Метод для регистрации пользователя.
     *
     * @param dto данные для регистрации.
     * @return dto с токеном и данными о пользователе.
     */
    FullPersonDto register(RegisterPersonDto dto);

    /**
     * Метод для аутентификации пользователя.
     *
     * @param dto данные для аутентификации.
     * @return dto с токеном и данными о пользователе.
     */
    FullPersonDto login(PersonCredentialsDto dto) throws UnauthorizedException;

}
