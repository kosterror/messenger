package ru.tsu.hits.kosterror.messenger.core.util;

import lombok.experimental.UtilityClass;

/**
 * Класс с константами для хэдеров запросов.
 */
@UtilityClass
public class HeaderValues {
    public static final String HEADER_JWT = "Authorization";

    public static final String HEADER_API_KEY = "Api-Key";

    public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
}
