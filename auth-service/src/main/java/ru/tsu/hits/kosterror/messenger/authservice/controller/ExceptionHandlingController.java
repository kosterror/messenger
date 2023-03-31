package ru.tsu.hits.kosterror.messenger.authservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.tsu.hits.kosterror.messenger.authservice.dto.ApiError;
import ru.tsu.hits.kosterror.messenger.authservice.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ExceptionHandlingController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError>
    handleInvalidRequestBody(HttpServletRequest request, MethodArgumentNotValidException exception) {
        logError(request, exception);

        Map<String, List<String>> messages = new HashMap<>();

        exception
                .getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();

                    if (message != null) {
                        if (messages.containsKey(fieldName)) {
                            messages.get(fieldName).add(message);
                        } else {
                            List<String> newMessageList = new ArrayList<>();
                            newMessageList.add(message);

                            messages.put(fieldName, newMessageList);
                        }
                    }

                });

        return new ResponseEntity<>(
                new ApiError(
                        HttpStatus.BAD_REQUEST,
                        "Тело запроса не прошло валидацию",
                        messages
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(HttpServletRequest request, NotFoundException exception) {
        logError(request, exception);

        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );

        return new ResponseEntity<>(error, error.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(HttpServletRequest request, Exception exception) {
        logError(request, exception);

        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Непредвиденная внутрення ошибка сервера"
        );


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
