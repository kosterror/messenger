package ru.tsu.hits.kosterror.messenger.chatservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.tsu.hits.kosterror.messenger.chatservice.validation.fileformat.ImageFormat;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateChatDto {

    @NotNull
    private String chatName;

    @NotNull
    @ImageFormat
    private MultipartFile avatar;

    @NotNull
    private List<UUID> membersId;

}
