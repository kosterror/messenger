package ru.tsu.hits.kosterror.messenger.core.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для параметров пагинации.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingParams {

    private int totalPage;
    private long totalElement;
    private int page;
    private int size;

}
