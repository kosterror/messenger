package ru.tsu.hits.kosterror.messenger.authservice.dto.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.messenger.authservice.util.validation.datebetween.DateBetween;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO для обновления профиля пользователя.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePersonDto {

    @NotBlank(message = "ФИО не может быть пустым")
    private String fullName;

    @DateBetween(startDate = "1900-01-01", message = "Дата должна быть больше '1900-01-01', но меньше текущей")
    private LocalDate birthDate;

    private String phoneNumber;

    private String city;

    private UUID avatarId;

}
