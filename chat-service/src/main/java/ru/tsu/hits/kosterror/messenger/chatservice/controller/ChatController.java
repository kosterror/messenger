package ru.tsu.hits.kosterror.messenger.chatservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.CreateChatDto;

import javax.validation.Valid;

@RequestMapping("/api/chat")
@RestController
@Slf4j
public class ChatController {


    @SneakyThrows
    @PostMapping(
            value = "/group/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "Создать беседу.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ChatDto createChat(@ModelAttribute @Valid CreateChatDto dto) {
        //TODO вызвать сервис
        return null;
    }

}
