package ru.tsu.hits.kosterror.messenger.coresecurity.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Класс представляющий собой конфигурацию для Spring Security из
 * файла application.yaml.
 */
@ConfigurationProperties("app.security")
@Data
public class SecurityProperties {

    private SecurityJwtProperties jwtToken;

    private SecurityIntegrationProperties integration;

}
