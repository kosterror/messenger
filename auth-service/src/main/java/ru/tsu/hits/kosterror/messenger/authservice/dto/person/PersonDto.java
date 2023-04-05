package ru.tsu.hits.kosterror.messenger.authservice.dto.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO с информацией о пользователе.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    private UUID id;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private String login;
    private String email;
    private String password;
    private String fullName;
    private LocalDate birthDate;
    private String phoneNumber;
    private String city;
    private String avatarId;

}
