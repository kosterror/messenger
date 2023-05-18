package ru.tsu.hits.kosterror.messenger.chatservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDto {
    private UUID id;
    private String name;
    private Long sizeInBytes;
}
