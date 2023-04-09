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

}
