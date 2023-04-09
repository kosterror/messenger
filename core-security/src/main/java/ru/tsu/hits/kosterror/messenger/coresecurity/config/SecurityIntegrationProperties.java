package ru.tsu.hits.kosterror.messenger.coresecurity.config;

import lombok.Data;

/**
 * Класс для хранения свойств для запросов между сервисами.
 */
@Data
public class SecurityIntegrationProperties {

    private String apiKey;

    private String rootPath;

}
