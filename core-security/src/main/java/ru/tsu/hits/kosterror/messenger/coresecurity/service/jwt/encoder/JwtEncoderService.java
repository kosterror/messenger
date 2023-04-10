package ru.tsu.hits.kosterror.messenger.coresecurity.service.jwt.encoder;

import java.util.UUID;

/**
 * Интерфейс, предоставляющий методы для генерации токена.
 */
public interface JwtEncoderService {

    /**
     * Метод для генерации JWT токена.
     *
     * @param login    логин пользователя.
     * @param id       идентификатор пользователя.
     * @param email    почта пользователя.
     * @param fullName ФИО пользователя.
     * @return JWT токен.
     */
    String generateToken(String login, UUID id, String email, String fullName);

}
