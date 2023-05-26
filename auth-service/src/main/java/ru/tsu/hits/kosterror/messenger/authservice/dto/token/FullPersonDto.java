package ru.tsu.hits.kosterror.messenger.authservice.dto.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;

/**
 * DTO с информацией о пользователе и access токеном.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullPersonDto {

    private String accessToken;

    private PersonDto personDto;

}
