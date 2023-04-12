package ru.tsu.hits.kosterror.messenger.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Простое DTO с единственным свойством типа boolean, чтобы составить красивое тело ответа.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooleanDto {

    private boolean value;

}
