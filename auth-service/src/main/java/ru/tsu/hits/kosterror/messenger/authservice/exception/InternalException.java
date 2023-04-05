package ru.tsu.hits.kosterror.messenger.authservice.exception;

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

}
