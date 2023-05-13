package ru.tsu.hits.kosterror.messenger.coresecurity.util;

import lombok.experimental.UtilityClass;

/**
 * Класс с константами для заголовков запросов.
 */
@UtilityClass
public class Constants {

    public static final String CLAIM_ID = "id";

    public static final String CLAIM_EMAIL = "email";

    public static final String CLAIM_FULL_NAME = "fullName";

    public static final String ISSUER = "messenger-core-security";

    public static final String JWT_PATTERN = "Bearer \\S+.\\S+.\\S+";

}
