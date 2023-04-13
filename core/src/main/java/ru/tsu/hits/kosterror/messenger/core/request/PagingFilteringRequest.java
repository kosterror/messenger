package ru.tsu.hits.kosterror.messenger.core.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * DTO для запроса на эндпоинты с пагинацией, учитывая фильтрацию
 *
 * @param <T> тип DTO с полями для фильтрации.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Объект с параметрами фильтрации и пагинации.")
public class PagingFilteringRequest<T> {

    @NotNull(message = "Параметры пагинации не могут быть null")
    @Schema(description = "Параметры пагинации.")
    private PagingParamsRequest paging;

    @NotNull(message = "Корневой объект параметров фильтрации не может быть null")
    @Schema(description = "Параметры фильтрации.")
    private T filters;

}
