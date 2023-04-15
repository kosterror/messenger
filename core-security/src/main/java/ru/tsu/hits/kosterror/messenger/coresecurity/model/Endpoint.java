package ru.tsu.hits.kosterror.messenger.coresecurity.model;

import lombok.Data;

/**
 * Класс для хранения данных об эндпоинте
 */
@Data
public class Endpoint {

    private String route;

    private String method;

}
