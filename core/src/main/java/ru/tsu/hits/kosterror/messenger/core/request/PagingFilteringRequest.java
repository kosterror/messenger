package ru.tsu.hits.kosterror.messenger.core.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запроса на эндпоинты с пагинацией, учитывая фильтрацию
 *
 * @param <T> тип DTO с полями для фильтрации.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingFilteringRequest<T> {

    private PagingParamsRequest paging;
    private T filters;

}
