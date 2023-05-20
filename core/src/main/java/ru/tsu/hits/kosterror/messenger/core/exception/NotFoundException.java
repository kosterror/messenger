package ru.tsu.hits.kosterror.messenger.core.exception;

/**
 * Исключение для случаев, когда какая-либо информация с заданными параметрами не найдена.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Конструктор.
     *
     * @param message сообщение об ошибке.
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Конструктор.
     *
     * @param message сообщение об ошибке.
     * @param cause   причина.
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
