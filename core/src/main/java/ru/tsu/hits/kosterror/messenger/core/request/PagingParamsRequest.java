package ru.tsu.hits.kosterror.messenger.core.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запросов на эндпоинты с пагинацией.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingParamsRequest {

    private int page;

    private int size;

}
