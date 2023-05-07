package ru.tsu.hits.kosterror.messenger.chatservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
    private UUID id;
    private String name;
    private UUID avatarId;
    private UUID adminId;
    private LocalDateTime creationDate;
}
