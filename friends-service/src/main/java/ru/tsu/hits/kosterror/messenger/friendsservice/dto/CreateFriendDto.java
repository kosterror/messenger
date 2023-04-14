package ru.tsu.hits.kosterror.messenger.friendsservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * DTO для добавления пользователя в друзья с информацией об этом пользователе.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Объект с информацией для добавления пользователя в друзья.")
public class CreateFriendDto {

    @NotNull(message = "Идентификатор нового друга не может быть null")
    @Schema(description = "Идентификатор нового друга.")
    private UUID memberId;

    @NotBlank(message = "ФИО нового друга не может быть пустым")
    @Schema(description = "ФИО нового друга.")
    private String memberFullName;

}
