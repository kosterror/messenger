package ru.tsu.hits.kosterror.messenger.coresecurity.security.authenticationtoken;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import ru.tsu.hits.kosterror.messenger.coresecurity.model.JwtPersonData;

/**
 * Класс для аутентификации пользователя по JWT для текущего запроса.
 */
public class JwtAuthentication extends AbstractAuthenticationToken {

    /**
     * Конструктор.
     *
     * @param jwtPersonData информация о пользователе из payload токена.
     */
    public JwtAuthentication(JwtPersonData jwtPersonData) {
        super(null);
        super.setDetails(jwtPersonData);

        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return super.getDetails();
    }

}
