package ru.tsu.hits.kosterror.messenger.core.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для параметров пагинации в ответе.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingParamsResponse {

    private int totalPage;
    private long totalElements;
    private int page;
    private int size;

}
