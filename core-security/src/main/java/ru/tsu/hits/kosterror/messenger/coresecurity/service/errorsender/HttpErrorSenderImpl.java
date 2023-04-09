package ru.tsu.hits.kosterror.messenger.coresecurity.service.errorsender;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.core.dto.ApiError;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Класс для отправки ошибок через {@link HttpServletResponse}, реализующий интерфейс {@link HttpErrorSender}.
 */
@Service
@RequiredArgsConstructor
public class HttpErrorSenderImpl implements HttpErrorSender {

    private static final String CHAR_ENCODING = "UTF-8";
    private final ObjectMapper objectMapper;

    @Override
    public void sendError(HttpServletResponse response,
                          int status,
                          String message,
                          Map<String, List<String>> errors
    ) throws IOException {
        ApiError error = new ApiError(status, message, errors);
        sendError(response, error);
    }

    @Override
    public void sendError(HttpServletResponse response,
                          int status,
                          String message
    ) throws IOException {
        ApiError error = new ApiError(status, message);
        sendError(response, error);
    }

    private void sendError(HttpServletResponse response, ApiError error) throws IOException {
        String responseBody = objectMapper.writeValueAsString(error);

        response.setStatus(error.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CHAR_ENCODING);
        response.getWriter().write(responseBody);
    }

}
