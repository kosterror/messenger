package ru.tsu.hits.kosterror.messenger.authservice.dto.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullPersonDto {

    private String accessToken;

    private PersonDto personDto;

}
