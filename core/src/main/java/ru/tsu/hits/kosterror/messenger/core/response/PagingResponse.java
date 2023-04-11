package ru.tsu.hits.kosterror.messenger.core.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для ответа на запросы с пагинацией.
 *
 * @param <T> тип контента.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingResponse<T> {

    private PagingParamsResponse paging;
    private T content;

}
