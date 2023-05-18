package ru.tsu.hits.kosterror.messenger.chatservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private UUID id;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime sendingDate;
    private String text;
    private UUID authorId;
    private String authorFullName;
    private UUID authorAvatarId;
    List<AttachmentDto> attachments;
}
