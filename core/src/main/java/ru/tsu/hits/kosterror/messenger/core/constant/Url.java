package ru.tsu.hits.kosterror.messenger.core.constant;

import lombok.experimental.UtilityClass;

/**
 * Класс с константами URL интеграционных запросов.
 */
@UtilityClass
public class Url {
    public static final String API_GATEWAY_PATH = "http://localhost:8080";
    public static final String FRIENDS_PERSON_IS_BLOCKED = "/integration/friends/blocked-persons";
}
