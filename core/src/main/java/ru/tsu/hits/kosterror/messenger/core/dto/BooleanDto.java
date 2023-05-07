package ru.tsu.hits.kosterror.messenger.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Простое DTO с единственным свойством типа boolean, чтобы составить красивое тело ответа.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Объект с булевским значением.")
public class BooleanDto {

    @NotNull(message = "Значение не может быть null")
    @Schema(description = "Булево значение.")
    private Boolean value;

}
