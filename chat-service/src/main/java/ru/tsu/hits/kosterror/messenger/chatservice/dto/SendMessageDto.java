package ru.tsu.hits.kosterror.messenger.chatservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

/**
 * DTO для отправки сообщения в беседу или диалог.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Объект для отправки сообщения в беседу или диалог.")
public class SendMessageDto {

    @NotNull
    @Schema(description = "Если сообщение отправляется в беседу, то это поле является id беседы," +
            "если диалог, то это поле является id пользователя, кому отправляется сообщение.")
    private UUID targetId;

    @Schema(description = "Текст сообщения.", example = "Осмысленный пример текста сообщения.")
    @Size(max = 500, message = "Длина сообщения не может превышать 500 символов.")
    private String text;

    @Size(max = 10, message = "Количество вложений не может быть больше 10.")
    @Schema(description = "Идентификаторы файлов вложений.")
    List<UUID> attachmentIds;

}

