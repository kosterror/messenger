package ru.tsu.hits.kosterror.messenger.core.exception;

/**
 * Исключение для случаев, когда какая-либо информация с заданными параметрами не найдена.
 */
public class NotFoundException extends Exception {

    /**
     * Конструктор.
     *
     * @param message сообщение об ошибке.
     */
    public NotFoundException(String message) {
        super(message);
    }

}
