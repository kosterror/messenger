package ru.tsu.hits.kosterror.messenger.core.exception;

/**
 * Исключение для непредвиденных внутренних ошибок.
 */
public class InternalException extends RuntimeException {

    /**
     * Конструктор.
     *
     * @param message текст исключения.
     */
    public InternalException(String message) {
        super(message);
    }

    /**
     * Конструктор.
     *
     * @param message текст исключения.
     * @param cause   исключение.
     */
    public InternalException(String message, Throwable cause) {
        super(message, cause);
    }

}
