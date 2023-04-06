package ru.tsu.hits.kosterror.messenger.authservice.service.servlet;

import ru.tsu.hits.kosterror.messenger.authservice.dto.ApiError;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Интерфейс сервиса для отправки ответов с использованием {@link HttpServletResponse}.
 */
public interface ServletResponseService {

    /**
     * Метод, который отправит ошибку с телом ответа в формате {@link ApiError}
     *
     * @param response   ответ.
     * @param statusCode статус код ответа.
     * @param message    текст об ошибке.
     * @throws IOException возникает при взаимодействии с {@link HttpServletResponse}.
     */
    void sendError(HttpServletResponse response, int statusCode, String message) throws IOException;

}
