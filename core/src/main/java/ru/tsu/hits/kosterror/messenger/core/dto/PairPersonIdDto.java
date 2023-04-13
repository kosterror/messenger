package ru.tsu.hits.kosterror.messenger.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * DTO для запросов, где нужно передать идентификаторы двух пользователей,
 * которые состоят в какой-то связи.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PairPersonIdDto {

    @NotNull(message = "Идентификатор не может быть null")
    private UUID ownerId;

    @NotNull(message = "Идентификатор не может быть null")
    private UUID memberId;

}
