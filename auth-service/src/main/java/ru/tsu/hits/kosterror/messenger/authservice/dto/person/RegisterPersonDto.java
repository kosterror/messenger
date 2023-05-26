package ru.tsu.hits.kosterror.messenger.authservice.dto.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.messenger.authservice.util.validation.datebetween.DateBetween;
import ru.tsu.hits.kosterror.messenger.authservice.util.validation.email.UniqueEmail;
import ru.tsu.hits.kosterror.messenger.authservice.util.validation.login.UniqueLogin;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * DTO для регистрации пользователя с валидацией.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPersonDto {

    @NotBlank(message = "Логин не может быть пустым")
    @UniqueLogin
    private String login;

    @NotBlank(message = "Почта не может быть пустой")
    @UniqueEmail
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    @NotBlank(message = "ФИО не может быть пустым")
    private String fullName;

    @DateBetween(startDate = "1900-01-01", message = "Дата должна быть больше '1900-01-01', но меньше текущей")
    private LocalDate birthDate;

    private String phoneNumber;

    private String city;

}
