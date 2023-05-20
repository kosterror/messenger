package ru.tsu.hits.kosterror.messenger.chatservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateChatDto {

    @NotNull
    private String chatName;

    private UUID avatarId;

    @NotNull
    @Size(min = 2, message = "Помимо создателя в беседе должны быть хотя бы два участника")
    private List<UUID> membersId;

}
