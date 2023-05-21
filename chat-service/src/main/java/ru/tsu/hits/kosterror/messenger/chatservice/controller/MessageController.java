package ru.tsu.hits.kosterror.messenger.chatservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.MessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.SendMessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.service.message.MessageService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor.extractId;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;


    @PostMapping("/message/send")
    @Operation(
            summary = "Отправить сообщение в чат.",
            description = "Сообщение может отправиться в личный чат и в беседу. " +
                    "Чтобы начать личный чат нужно сперва использовать запрос на отправку " +
                    "сообщения в личный диалог (\"Отправить сообщение в личный диалог.\").",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void sendMessageToGroupChat(Authentication auth,
                                       @RequestBody @Valid SendMessageDto dto) {
        messageService.sendMessageToChat(extractId(auth), dto);
    }

    @PostMapping("/private/message/send")
    @Operation(
            summary = "Отправить сообщение в личный диалог.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public void sendMessageToPrivateChat(Authentication auth,
                                         @RequestBody @Valid SendMessageDto dto) {
        messageService.sendMessageToPerson(extractId(auth), dto);
    }

    @GetMapping("/{chatId}/message")
    @Operation(
            summary = "Получить сообщения чата.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public List<MessageDto> getMessages(Authentication auth,
                                        @PathVariable UUID chatId) {
        return messageService.getChatMessages(extractId(auth), chatId);
    }

    @GetMapping("/search-message")
    @Operation(
            summary = "Поиск сообщения.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public List<MessageDto> findMessages(Authentication auth,
                                         @RequestParam String searchSubstring) {
        return messageService.findMessages(extractId(auth), searchSubstring);
    }

}
