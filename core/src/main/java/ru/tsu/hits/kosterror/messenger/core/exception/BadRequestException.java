package ru.tsu.hits.kosterror.messenger.core.exception;

/**
 * Исключение для случаев, когда пришли некорректные входные параметры.
 */
public class BadRequestException extends RuntimeException {

    /**
     * Конструктор.
     *
     * @param message сообщение об ошибке.
     */
    public BadRequestException(String message) {
        super(message);
    }

}
