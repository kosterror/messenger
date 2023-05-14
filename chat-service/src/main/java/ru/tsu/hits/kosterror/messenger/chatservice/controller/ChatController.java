package ru.tsu.hits.kosterror.messenger.chatservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.CreateUpdateChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.service.chatmanage.ChatManageService;

import javax.validation.Valid;
import java.util.UUID;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor.extractId;

@RequestMapping("/api/chat")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatManageService service;

    @PostMapping("/group/create")
    @Operation(
            summary = "Создать беседу.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ChatDto createChat(Authentication auth, @RequestBody @Valid CreateUpdateChatDto dto) {
        return service.createGroupChat(extractId(auth), dto);
    }

    @PutMapping("/group/{chatId}")
    @Operation(
            summary = "Изменить беседу.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ChatDto updateChat(Authentication auth,
                              @PathVariable UUID chatId,
                              @RequestBody @Valid CreateUpdateChatDto dto) {
        return service.updateGroupChat(extractId(auth), chatId, dto);
    }
}
