package ru.tsu.hits.kosterror.messenger.coresecurity.service.errorsender;

import ru.tsu.hits.kosterror.messenger.core.response.ApiError;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Интерфейс, предоставляющий методы для отправки ошибки в формате {@link ApiError} в теле ответа.
 */
public interface HttpErrorSender {

    /**
     * Метод для отправки ошибки.
     *
     * @param response ответ, в которые запишется ошибка.
     * @param status   статус код ответа.
     * @param message  основное сообщение об ошибке.
     * @param errors   список подвидов ошибок.
     * @throws IOException может возникнуть при записи ответа.
     */
    void sendError(HttpServletResponse response,
                   int status,
                   String message,
                   Map<String, List<String>> errors
    ) throws IOException;

    /**
     * Метод для отправки ошибки.
     *
     * @param response ответ, в которые запишется ошибка.
     * @param status   статус код ответа.
     * @param message  основное сообщение об ошибке.
     * @throws IOException может возникнуть при записи ответа.
     */
    void sendError(HttpServletResponse response, int status, String message) throws IOException;

}
