package ru.tsu.hits.kosterror.messenger.authservice.service.account;

import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.UpdatePersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.request.PersonPageRequest;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;

import java.util.List;

/**
 * Интерфейс для взаимодействия с профилями пользователей.
 */
public interface AccountService {

    /**
     * Метод для получения пользователя по логину.
     *
     * @param login логин пользователя.
     * @return информация о найденном пользователе.
     * @throws NotFoundException возникает, если пользователя с таким логином не найден.
     */
    PersonDto getAccountInfo(String login) throws NotFoundException;

    /**
     * Метод для обновления профиля пользователя.
     *
     * @param login логин пользователя.
     * @param dto   новая информация о пользователе.
     * @return обновленная информация о пользователе.
     * @throws NotFoundException возникает, если пользователь с таким логином не найден.
     */
    PersonDto updateAccount(String login, UpdatePersonDto dto) throws NotFoundException;

    /**
     * Метод для получения пользователей с учетом фильтрации, сортировки.
     *
     * @param personPageRequest информация о пагинации, фильтрации, сортировки.
     * @return найденные пользователи с информацией о пагинации, обернутые в {@link PagingResponse}.
     */
    PagingResponse<List<PersonDto>> getPersons(PersonPageRequest personPageRequest);
}
