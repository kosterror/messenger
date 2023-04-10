package ru.tsu.hits.kosterror.messenger.core.exception;

/**
 * Исключение для тех случаев, когда пользователь не прошел аутентификацию.
 */
public class UnauthorizedException extends RuntimeException {

    /**
     * Конструктор.
     *
     * @param message сообщение об ошибке.
     */
    public UnauthorizedException(String message) {
        super(message);
    }

}
