package ru.tsu.hits.kosterror.messenger.authservice.service.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.core.dto.ApiError;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Класс реализующий интерфейс {@link ServletResponseService}.
 */
@Service
@RequiredArgsConstructor
public class ServletResponseServiceImpl implements ServletResponseService {

    private static final String CHAR_ENCODING = "UTF-8";
    private final ObjectMapper objectMapper;

    @Override
    public void sendError(HttpServletResponse response, int statusCode, String message) throws IOException {
        ApiError error = new ApiError(
                statusCode,
                message
        );

        String responseBody = objectMapper.writeValueAsString(error);

        response.setStatus(statusCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CHAR_ENCODING);
        response.getWriter().write(responseBody);
    }

}
