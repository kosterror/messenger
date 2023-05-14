package ru.tsu.hits.kosterror.messenger.chatservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.CreateChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.service.chatmanage.ChatManageService;

import javax.validation.Valid;

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
    public Object createChat(Authentication auth, @RequestBody @Valid CreateChatDto dto) {
        return service.createGroupChat(extractId(auth), dto);
    }

}
