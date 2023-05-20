package ru.tsu.hits.kosterror.messenger.chatservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private UUID personId;
    private String fullName;
    private UUID avatarId;
}
