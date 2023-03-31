package ru.tsu.hits.kosterror.messenger.authservice.dto.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.messenger.authservice.dto.Gender;
import ru.tsu.hits.kosterror.messenger.authservice.util.validation.birthdate.BirthDateValidation;
import ru.tsu.hits.kosterror.messenger.authservice.util.validation.email.UniqueEmail;
import ru.tsu.hits.kosterror.messenger.authservice.util.validation.login.UniqueLogin;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPersonDto {

    @NotBlank(message = "Логин не может быть пустым")
    @UniqueLogin
    private String login;

    @BirthDateValidation
    private LocalDate birthDate;

    @NotBlank(message = "Почта не может быть пустой")
    @UniqueEmail
    private String email;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    @NotBlank(message = "Фамилия не может быть пустой")
    private String surname;

    private String patronymic;

    @NotNull(message = "Пол не может быть null")
    private Gender gender;

}
