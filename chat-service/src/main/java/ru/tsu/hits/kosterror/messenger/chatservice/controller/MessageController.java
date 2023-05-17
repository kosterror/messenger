package ru.tsu.hits.kosterror.messenger.chatservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.SendMessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.service.message.MessageService;

import javax.validation.Valid;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor.extractId;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;


    @PostMapping("/group/message")
    @Operation(
            summary = "Отправить сообщение в беседу.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void sendMessageToGroupChat(Authentication auth,
                                       @RequestBody @Valid SendMessageDto dto) {
        messageService.sendMessageToGroupChat(extractId(auth), dto);
    }

    @PostMapping("/private/message")
    @Operation(
            summary = "Отправить сообщение в личный диалог.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void sendMessageToPrivateChat(Authentication auth,
                                         @RequestBody @Valid SendMessageDto dto) {
        messageService.sendMessageToPrivateChat(extractId(auth), dto);
    }

}
