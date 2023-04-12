package ru.tsu.hits.kosterror.messenger.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO для запросов, где нужно передать идентификаторы двух пользователей,
 * которые состоят в какой-то связи.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PairPersonIdDto {

    private UUID ownerId;

    private UUID memberId;

}
