package ru.tsu.hits.kosterror.messenger.chatservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatFilters;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatMessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.CreateUpdateChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.service.chatinfo.ChatInfoService;
import ru.tsu.hits.kosterror.messenger.chatservice.service.chatmanage.ChatManageService;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.JwtExtractor.extractId;

@RequestMapping("/api/chat")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {


    private final ChatManageService chatManageService;
    private final ChatInfoService chatInfoService;

    @PostMapping("/group")
    @Operation(
            summary = "Создать беседу.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ChatDto createChat(Authentication auth, @RequestBody @Valid CreateUpdateChatDto dto) {
        return chatManageService.createGroupChat(extractId(auth), dto);
    }

    @PutMapping("/group/{chatId}")
    @Operation(
            summary = "Изменить беседу.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ChatDto updateChat(Authentication auth,
                              @PathVariable UUID chatId,
                              @RequestBody @Valid CreateUpdateChatDto dto) {
        return chatManageService.updateGroupChat(extractId(auth), chatId, dto);
    }

    @GetMapping("/{chatId}")
    @Operation(
            summary = "Получить информацию о чате.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ChatDto getChat(Authentication auth,
                           @PathVariable UUID chatId) {
        return chatInfoService.findChatById(extractId(auth), chatId);
    }

    @PostMapping("/search")
    @Operation(
            summary = "Получить список чатов.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public PagingResponse<List<ChatMessageDto>> getChats(Authentication auth,
                                                         @RequestBody PagingFilteringRequest<ChatFilters> request) {
        return chatInfoService.getChats(extractId(auth), request);
    }


}
