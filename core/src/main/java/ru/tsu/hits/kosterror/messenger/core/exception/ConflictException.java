package ru.tsu.hits.kosterror.messenger.core.exception;

/**
 * Исключение для случаев, когда возникают противоречивые ситуации.
 */
public class ConflictException extends RuntimeException {

    /**
     * Конструктор.
     *
     * @param message текст исключения.
     */
    public ConflictException(String message) {
        super(message);
    }

}
