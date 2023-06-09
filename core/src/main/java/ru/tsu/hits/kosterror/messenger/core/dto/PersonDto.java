package ru.tsu.hits.kosterror.messenger.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO с информацией о пользователе.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String login;
    private String email;
    private String fullName;
    private LocalDate registerDate;
    private LocalDate birthDate;
    private String phoneNumber;
    private String city;
    private UUID avatarId;

}
