package ru.tsu.hits.kosterror.messenger.friendsservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;

/**
 * Класс для создания {@link Pageable}.
 */
@Component
@Slf4j
public class PageableBuilder {

    /**
     * Метод для создания {@link Pageable} на основе параметров.
     *
     * @param page       номер страницы.
     * @param size       размер страницы.
     * @param direction  направление сортировки.
     * @param properties параметры для сортировки.
     * @return объект {@link Pageable}, построенный на основе исходных параметров.
     */
    public Pageable build(int page, int size, Sort.Direction direction, String... properties) {
        if (page < 0) {
            log.error("Некорректный номер страницы: {} (размер страницы: {})", page, size);
            throw new BadRequestException("Номер страницы не может быть меньше нуля");
        }

        if (size < 1) {
            log.error("Некорректный размер страницы: {} (номер страницы: {})", size, page);
            throw new BadRequestException("Номер страницы не может быть меньше 1");
        }

        return PageRequest.of(page, size, direction, properties);
    }

}
