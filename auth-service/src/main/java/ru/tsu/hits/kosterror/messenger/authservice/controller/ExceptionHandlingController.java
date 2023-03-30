package ru.tsu.hits.kosterror.messenger.authservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.tsu.hits.kosterror.messenger.authservice.dto.ApiError;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
@Slf4j
public class ExceptionHandlingController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(HttpServletRequest request, Exception exception) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                List.of("Непредвиденная внутрення ошибка сервера")
        );

        logError(request, exception);

        return new ResponseEntity<>(error, error.getHttpStatus());
    }

    private void logError(HttpServletRequest request, Exception exception) {
        log.error("Ошибка во время выполнения на URL: {} {}. {}",
                request.getMethod(),
                request.getRequestURL(),
                exception.getMessage(),
                exception
        );
    }

}
