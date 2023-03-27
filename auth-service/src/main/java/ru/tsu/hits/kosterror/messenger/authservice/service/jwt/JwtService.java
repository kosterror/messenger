package ru.tsu.hits.kosterror.messenger.authservice.service.jwt;

import ru.tsu.hits.kosterror.messenger.authservice.exception.TokenVerificationException;

public interface JwtService {

    String generateAccessToken(String login);

    String generateRefreshToken(String login);

    String extractLogin(String token, String secret) throws TokenVerificationException;

}
