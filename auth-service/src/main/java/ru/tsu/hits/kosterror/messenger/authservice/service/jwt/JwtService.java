package ru.tsu.hits.kosterror.messenger.authservice.service.jwt;

import ru.tsu.hits.kosterror.messenger.authservice.exception.TokenVerificationException;

public interface JwtService {

    String generateAccessToken(String login);

    String generateRefreshToken(String login);

    String extractLoginFromAccessToken(String token) throws TokenVerificationException;

    String extractLoginFromRefreshToken(String token) throws TokenVerificationException;

}
