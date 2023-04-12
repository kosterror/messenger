package ru.tsu.hits.kosterror.messenger.authservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import ru.tsu.hits.kosterror.messenger.core.request.PagingParamsRequest;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * DTO для запроса всех пользователей.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonPageRequest {

    @NotNull(message = "Параметры пагинации не могут быть null")
    private PagingParamsRequest pagingParams;

    private Map<String, Sort.Direction> sortProperties;

    private PersonFilters personFilters;

}
