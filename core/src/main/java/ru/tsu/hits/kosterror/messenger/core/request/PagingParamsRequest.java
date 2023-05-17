package ru.tsu.hits.kosterror.messenger.core.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

/**
 * DTO для запросов на эндпоинты с пагинацией.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Объект с параметрами пагинации.")
public class PagingParamsRequest {

    @Min(0)
    @Schema(description = "Номер страницы.", minimum = "0")
    private Integer page = 0;

    @Min(1)
    @Schema(description = "Размер страницы.", minimum = "1")
    private Integer size = 10;

}
