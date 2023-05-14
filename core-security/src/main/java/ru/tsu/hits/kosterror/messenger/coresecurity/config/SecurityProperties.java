package ru.tsu.hits.kosterror.messenger.coresecurity.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

/**
 * Класс представляющий собой конфигурацию для Spring Security из
 * файла application.yaml.
 */
@Slf4j
@ConfigurationProperties("app.security")
@Data
public class SecurityProperties {

    private SecurityJwtProperties jwtToken;

    private SecurityIntegrationProperties integration;

    @PostConstruct
    private void init() {
        log.info("SecurityProperties: {}", this);
    }

}
