package ru.tsu.hits.kosterror.messenger.authservice.service.jwt.accesstoken;

import ru.tsu.hits.kosterror.messenger.authservice.exception.TokenVerificationException;

public interface AccessTokenService {

    String generateToken(String login);

    String extractLogin(String token) throws TokenVerificationException;

}
