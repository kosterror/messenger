package ru.tsu.hits.kosterror.messenger.core.response;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Объект с информацией о пагинации и контентом для возврата ответа.")
public class PagingResponse<T> {

    private PagingParamsResponse paging;
    private T content;

}
