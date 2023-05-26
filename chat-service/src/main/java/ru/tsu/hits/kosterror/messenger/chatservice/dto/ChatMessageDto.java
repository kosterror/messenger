package ru.tsu.hits.kosterror.messenger.chatservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Информация о чате и последнем сообщении о нём.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    private UUID id;

    private String name;

    private String lastMessage;

    private Boolean isHasAttachment;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime lastMessageDateTime;

    private UUID lastMessageAuthor;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime creationDateTime;
}
