package ru.tsu.hits.kosterror.messenger.authservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.tsu.hits.kosterror.messenger.core.exception.*;
import ru.tsu.hits.kosterror.messenger.core.response.ApiError;

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
                HttpStatus.BAD_REQUEST.value(),
                "Тело запроса не прошло валидацию",
                messages
        );

        return buildResponseEntity(error);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handlerBadRequestException(HttpServletRequest request,
                                                               BadRequestException exception
    ) {
        logError(request, exception);

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage()
        );

        return buildResponseEntity(error);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(HttpServletRequest request,
                                                                UnauthorizedException exception
    ) {
        logError(request, exception);

        ApiError error = new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                exception.getMessage()
        );

        return buildResponseEntity(error);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiError> handleForbiddenException(HttpServletRequest request, ForbiddenException exception) {
        logError(request, exception);

        ApiError error = new ApiError(
                HttpStatus.FORBIDDEN.value(),
                exception.getMessage()
        );

        return buildResponseEntity(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(HttpServletRequest request, NotFoundException exception) {
        logError(request, exception);

        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage()
        );

        return buildResponseEntity(error);
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<ApiError> handleInternalException(HttpServletRequest request, InternalException exception) {
        logError(request, exception);

        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Внутрення ошибка сервера"
        );

        return buildResponseEntity(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(HttpServletRequest request, Exception exception) {
        logError(request, exception);

        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Непредвиденная внутрення ошибка сервера"
        );

        return buildResponseEntity(error);
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError error) {
        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
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
