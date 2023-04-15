package ru.tsu.hits.kosterror.messenger.coresecurity.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import ru.tsu.hits.kosterror.messenger.coresecurity.model.JwtPersonData;

import java.util.UUID;

/**
 * Класс для извлечения {@link JwtPersonData} из {@link Authentication}.
 */
@UtilityClass
public class JwtExtractor {

    /**
     * Метод для извлечения информации о пользователя в виде {@link JwtPersonData}
     * из {@link Authentication} текущего запроса.
     *
     * @param authentication информация о пользователя для текущего HTTP запроса.
     * @return информация о пользователе в {@link JwtPersonData}
     */
    public static JwtPersonData extractPersonData(Authentication authentication) {
        return (JwtPersonData) authentication.getPrincipal();
    }

    /**
     * Метод для извлечения идентификатора из {@link Authentication} текущего запроса.
     *
     * @param authentication информация о пользователя для текущего HTTP запроса.
     * @return идентификатор пользователя.
     */
    public static UUID extractId(Authentication authentication) {
        return ((JwtPersonData) authentication.getPrincipal()).getId();
    }

}
