package ru.tsu.hits.kosterror.messenger.authservice.service.jwt.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.authservice.dto.ApiError;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServletResponseServiceImpl implements ServletResponseService {

    private static final String CONTENT_TYPE = "application/json";
    private static final String CHAR_ENCODING = "UTF-8";
    private final ObjectMapper objectMapper;

    @Override
    public void sendError(HttpServletResponse response, int statusCode, String... errorMessage) throws IOException {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.valueOf(statusCode),
                List.of(errorMessage)
        );

        String responseBody = objectMapper.writeValueAsString(error);

        response.setStatus(statusCode);
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHAR_ENCODING);

        PrintWriter responseWriter = response.getWriter();
        responseWriter.print(responseBody);
        responseWriter.flush();
    }

}
