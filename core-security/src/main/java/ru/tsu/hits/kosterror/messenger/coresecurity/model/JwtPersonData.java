package ru.tsu.hits.kosterror.messenger.coresecurity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * Класс, представляющий собой информацию о пользователе, полученную из токена.
 */
@Getter
@AllArgsConstructor
public class JwtPersonData {

    private final String login;
    private final String fullName;
    private final String email;
    private UUID id;

}
