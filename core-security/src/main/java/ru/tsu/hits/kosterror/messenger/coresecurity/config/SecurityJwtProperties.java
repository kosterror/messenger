package ru.tsu.hits.kosterror.messenger.coresecurity.config;

import lombok.Data;

/**
 * Класс для хранения свойств JWT токена из application.yaml.
 */
@Data
public class SecurityJwtProperties {

    private String rootPath;

    private String[] permitAllEndpoints;

    private String secretKey;

    private Long expirationMinutes;

}
