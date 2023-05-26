package ru.tsu.hits.kosterror.messenger.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс для тел запросов с одним параметром в виде числа.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NumberDto {

    private Integer value;

}
