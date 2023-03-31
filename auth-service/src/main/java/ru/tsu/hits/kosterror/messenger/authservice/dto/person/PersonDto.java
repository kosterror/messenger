package ru.tsu.hits.kosterror.messenger.authservice.dto.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.messenger.authservice.dto.Gender;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    private UUID id;
    private String login;
    private LocalDate birthDate;
    private String email;
    private String name;
    private String password;
    private String surname;
    private String patronymic;
    private Gender gender;

}
