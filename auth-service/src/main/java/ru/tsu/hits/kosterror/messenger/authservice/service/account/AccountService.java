package ru.tsu.hits.kosterror.messenger.authservice.service.account;

import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.UpdatePersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.exception.NotFoundException;

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

}
