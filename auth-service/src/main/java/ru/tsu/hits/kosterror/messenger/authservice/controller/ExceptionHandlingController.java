package ru.tsu.hits.kosterror.messenger.authservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.tsu.hits.kosterror.messenger.authservice.dto.ApiError;
import ru.tsu.hits.kosterror.messenger.authservice.util.constant.HeaderKeys;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.exception.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Контроллер для отлавливания исключений, которые идут на rest контроллеры.
 */
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

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Тело запроса не прошло валидацию",
                messages
        );

        return buildResponseEntity(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(HttpServletRequest request,
                                                                UnauthorizedException exception
    ) {
        logError(request, exception);

        ApiError error = new ApiError(
                HttpStatus.UNAUTHORIZED,
                exception.getMessage()
        );

        return buildResponseEntity(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(HttpServletRequest request, NotFoundException exception) {
        logError(request, exception);

        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );

        return buildResponseEntity(error);
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ApiError> handleInternalException(HttpServletRequest request, InternalException exception) {
        logError(request, exception);

        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Внутрення ошибка сервера"
        );

        return buildResponseEntity(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(HttpServletRequest request, Exception exception) {
        logError(request, exception);

        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Непредвиденная внутрення ошибка сервера"
        );

        return buildResponseEntity(error);
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError error) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HeaderKeys.HANDLED_EXCEPTION, "yes");

        return new ResponseEntity<>(error, responseHeaders, error.getHttpStatus());
    }

    private void logError(HttpServletRequest request, Exception exception) {
        log.error("Ошибка во время выполнения запроса на URL: {} {}. {}",
                request.getMethod(),
                request.getRequestURL(),
                exception.getMessage(),
                exception
        );
    }

}
