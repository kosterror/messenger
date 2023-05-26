package ru.tsu.hits.kosterror.messenger.core.util;

import lombok.experimental.UtilityClass;

/**
 * Класс с названиями очередей {@code RabbitMQ} для отправки сообщений.
 */
@UtilityClass
public class RabbitMQBindings {
    public static final String CREATE_NOTIFICATION_OUT = "createNotification-out-0";
    public static final String SYNCHRONIZE_PERSON_DETAILS_OUT = "synchronizePersonDetails-out-0";
}