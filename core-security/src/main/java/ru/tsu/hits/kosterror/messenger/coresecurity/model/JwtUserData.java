package ru.tsu.hits.kosterror.messenger.coresecurity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * Класс, представляющий собой информацию о пользователе, полученную из токена.
 */
@Getter
@AllArgsConstructor
public class JwtUserData {

    private final String login;
    private final String fullName;
    private UUID id;

}
