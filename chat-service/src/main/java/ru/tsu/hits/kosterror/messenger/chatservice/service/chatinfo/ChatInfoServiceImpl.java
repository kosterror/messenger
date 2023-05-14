package ru.tsu.hits.kosterror.messenger.chatservice.service.chatinfo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Chat;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.RelationPerson;
import ru.tsu.hits.kosterror.messenger.chatservice.mapper.ChatMapper;
import ru.tsu.hits.kosterror.messenger.chatservice.repository.ChatRepository;
import ru.tsu.hits.kosterror.messenger.core.exception.ForbiddenException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatInfoServiceImpl implements ChatInfoService {

    private static final String NO_ACCESS_TO_THE_CHAT_MESSAGE = "Нет доступа к чату";
    private static final String CHAT_NOT_FOUND = "Чат с таким идентификатором не найден";
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;

    @Override
    public Chat findChatEntityById(UUID personId, UUID chatId) {
        Chat chat = chatRepository
                .findById(chatId)
                .orElseThrow(() -> new NotFoundException(CHAT_NOT_FOUND));

        if (!hasAccessToChat(personId, chat)) {
            throw new ForbiddenException(NO_ACCESS_TO_THE_CHAT_MESSAGE);
        }

        return chat;
    }

    @Override
    public ChatDto findChatById(UUID personId, UUID chatId) {
        Chat chat = findChatEntityById(personId, chatId);
        return chatMapper.entityToDto(chat);
    }

    private boolean hasAccessToChat(UUID personId, Chat chat) {
        List<RelationPerson> members = chat.getMembers();

        List<RelationPerson> membersWithSameId = members
                .stream()
                .filter(member -> member.getPersonId().equals(personId))
                .collect(Collectors.toList());

        return !membersWithSameId.isEmpty();
    }

}
