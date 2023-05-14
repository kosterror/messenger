package ru.tsu.hits.kosterror.messenger.chatservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateChatDto {

    @NotNull
    private String chatName;

    private UUID avatarId;

    @NotNull
    private List<UUID> membersId;

}
