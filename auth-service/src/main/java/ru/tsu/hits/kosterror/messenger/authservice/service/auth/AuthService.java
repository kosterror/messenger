package ru.tsu.hits.kosterror.messenger.authservice.service.auth;

import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonCredentialsDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.RegisterPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.token.PairTokenDto;
import ru.tsu.hits.kosterror.messenger.authservice.exception.UnauthorizedException;

public interface AuthService {

    PairTokenDto register(RegisterPersonDto dto);

    PairTokenDto login(PersonCredentialsDto dto) throws UnauthorizedException;

}
