package ru.tsu.hits.kosterror.messenger.coresecurity.security.authenticationtoken;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Класс для аутентификации интеграционных запросов по api-key для текущего запроса..
 */
public class IntegrationAuthentication extends AbstractAuthenticationToken {

    /**
     * Конструктор.
     */
    public IntegrationAuthentication() {
        super(null);
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

}
