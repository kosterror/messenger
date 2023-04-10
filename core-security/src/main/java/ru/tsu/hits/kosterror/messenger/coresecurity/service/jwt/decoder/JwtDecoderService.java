package ru.tsu.hits.kosterror.messenger.coresecurity.service.jwt.decoder;

import ru.tsu.hits.kosterror.messenger.core.exception.UnauthorizedException;
import ru.tsu.hits.kosterror.messenger.coresecurity.model.JwtPersonData;

/**
 * Интерфейс, предоставляющий методы для верификации JWT токена и извлечения информации из payload токена.
 */
public interface JwtDecoderService {

    /**
     * Методы для верификации JWT токена и извлечения информации из payload токена.
     *
     * @param token JWT токен.
     * @return информация из payload токена.
     * @throws UnauthorizedException возникает в случае ошибки во время верификации токена
     *                               или извлечения информации оттуда.
     */
    JwtPersonData getPersonData(String token);

}
