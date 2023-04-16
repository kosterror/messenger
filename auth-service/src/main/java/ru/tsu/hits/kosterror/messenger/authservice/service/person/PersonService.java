package ru.tsu.hits.kosterror.messenger.authservice.service.person;

import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.UpdatePersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.request.PersonPageRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс для взаимодействия с профилями пользователей.
 */
public interface PersonService {

    /**
     * Метод для получения информации о пользователе.
     *
     * @param personId идентификатор пользователя.
     * @return объект {@link PersonDto} с информацией о пользователе.
     */
    PersonDto getPersonInfo(UUID personId);

    /**
     * Метод для получения пользователя по логину.
     *
     * @param login логин пользователя.
     * @return информация о найденном пользователе.
     */
    PersonDto getMyPersonInfo(String login);

    /**
     * Метод для обновления профиля пользователя.
     *
     * @param login логин пользователя.
     * @param dto   новая информация о пользователе.
     * @return обновленная информация о пользователе.
     */
    PersonDto updatePersonInfo(String login, UpdatePersonDto dto);

    /**
     * Метод для получения пользователей с учетом фильтрации, сортировки.
     *
     * @param personPageRequest информация о пагинации, фильтрации, сортировки.
     * @return найденные пользователи с информацией о пагинации, обернутые в {@link PagingResponse}.
     */
    PagingResponse<List<PersonDto>> getPersons(PersonPageRequest personPageRequest);

    /**
     * Метод для получения информации о профиле другого человека.
     *
     * @param askerPersonLogin логин того пользователя, кто запрашивает информацию о профиле.
     * @param askedPersonLogin логин того пользователя, чью информацию о профиле запрашивают.
     * @return информация о профиле пользователя с логином {@code askedPersonLogin}
     */
    PersonDto getPersonInfo(String askerPersonLogin, String askedPersonLogin);
}
