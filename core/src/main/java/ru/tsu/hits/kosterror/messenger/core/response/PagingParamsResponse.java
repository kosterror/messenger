package ru.tsu.hits.kosterror.messenger.core.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для параметров пагинации в ответе.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Объект для возврата ответа о пагинации")
public class PagingParamsResponse {

    private int totalPage;
    private long totalElements;
    private int page;
    private int size;

}
