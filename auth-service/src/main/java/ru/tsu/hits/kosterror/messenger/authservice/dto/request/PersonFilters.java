package ru.tsu.hits.kosterror.messenger.authservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Параметры фильтрации пользователей.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonFilters {

    private String login;
    private String email;
    private String fullName;
    private LocalDate birthDate;
    private String phoneNumber;
    private String city;

}
