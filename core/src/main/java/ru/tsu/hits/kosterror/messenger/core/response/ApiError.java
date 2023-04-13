package ru.tsu.hits.kosterror.messenger.core.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DTO для тела ответа в случае возникновения ошибки.
 */
@Data
@NoArgsConstructor
@Schema(description = "Объект с информацией об ошибке.")
public class ApiError {
    private LocalDateTime timestamp;
    private Integer status;
    private String message;
    private Map<String, List<String>> errors;

    public ApiError(Integer status, String message, Map<String, List<String>> errors) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(Integer status, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.errors = new HashMap<>();
    }

}
