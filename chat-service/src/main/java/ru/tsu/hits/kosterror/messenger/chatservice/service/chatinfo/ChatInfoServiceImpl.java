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

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;

    @Override
    public ChatDto findChatById(UUID personId, UUID chatId) {
        Chat chat = chatRepository
                .findById(chatId)
                .orElseThrow(() -> new NotFoundException("Чат с таким идентификатором не найден"));

        if (!hasAccessToChat(personId, chat)) {
            throw new ForbiddenException("Нет прав на просмотр чата.");
        }

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
