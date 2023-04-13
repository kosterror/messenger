package ru.tsu.hits.kosterror.messenger.core.exception;

/**
 * Исключение, которое стоит использовать, когда какой-то ресурс запрещен.
 */
public class ForbiddenException extends RuntimeException {

    /**
     * Конструктор.
     *
     * @param message текст ошибки.
     */
    public ForbiddenException(String message) {
        super(message);
    }

}
