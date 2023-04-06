package ru.tsu.hits.kosterror.messenger.authservice.service.token;

/**
 * Интерфейс сервиса для работы с JWT токенами.
 */
public interface TokenService {

    /**
     * Метод для генерации токена для пользователя с заданным логином.
     *
     * @param login логин пользователя.
     * @return токен.
     */
    String generateToken(String login);

}
