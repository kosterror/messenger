package ru.tsu.hits.kosterror.messenger.coresecurity.config;

import lombok.Data;
import ru.tsu.hits.kosterror.messenger.coresecurity.model.Endpoint;

import java.util.List;

/**
 * Класс для хранения свойств JWT токена из application.yaml.
 */
@Data
public class SecurityJwtProperties {

    private String rootPath;

    private List<Endpoint> permitAllEndpoints;

    private String secretKey;

    private Long expirationMinutes;

}
