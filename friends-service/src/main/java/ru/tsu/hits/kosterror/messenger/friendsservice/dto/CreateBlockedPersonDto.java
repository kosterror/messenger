package ru.tsu.hits.kosterror.messenger.friendsservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * DTO для добавления пользователя в черный список.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Объект с информацией для добавления пользователя в черный список.")
public class CreateBlockedPersonDto {

    @NotNull(message = "Идентификатор не может быть null")
    @Schema(description = "Идентификатор пользователя, которого нужно заблокировать.")
    private UUID id;

    @NotBlank(message = "ФИО не может быть пустым")
    @Schema(description = "ФИО пользователя, которого нужно заблокировать.")
    private String fullName;

}
