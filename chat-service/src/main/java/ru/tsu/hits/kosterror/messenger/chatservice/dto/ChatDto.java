package ru.tsu.hits.kosterror.messenger.chatservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.messenger.chatservice.enumeration.ChatType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
    private UUID id;

    private ChatType type;

    private String name;

    private UUID avatarId;

    private UUID adminId;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime creationDate;

    private List<MemberDto> members;

}
