package ru.tsu.hits.kosterror.messenger.authservice.service.auth;

import ru.tsu.hits.kosterror.messenger.authservice.dto.person.RegisterPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.token.PairTokenDto;

public interface AuthService {

    PairTokenDto register(RegisterPersonDto dto);

}
