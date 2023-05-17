package ru.tsu.hits.kosterror.messenger.chatservice.service.chatmanage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.CreateUpdateChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Chat;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.RelationPerson;
import ru.tsu.hits.kosterror.messenger.chatservice.enumeration.ChatType;
import ru.tsu.hits.kosterror.messenger.chatservice.mapper.ChatMapper;
import ru.tsu.hits.kosterror.messenger.chatservice.repository.ChatRepository;
import ru.tsu.hits.kosterror.messenger.chatservice.service.person.RelationPersonService;
import ru.tsu.hits.kosterror.messenger.core.dto.BooleanDto;
import ru.tsu.hits.kosterror.messenger.core.dto.PairPersonIdDto;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.integration.friends.FriendIntegrationService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatManageServiceImpl implements ChatManageService {

    private final ChatRepository chatRepository;
    private final FriendIntegrationService friendIntegrationService;
    private final RelationPersonService relationPersonService;
    private final ChatMapper chatMapper;

    @Override
    public Chat createPrivateChat(RelationPerson first, RelationPerson second) {
        Chat chat = Chat
                .builder()
                .type(ChatType.PRIVATE)
                .creationDate(LocalDateTime.now())
                .members(List.of(first, second))
                .build();

        return chatRepository.save(chat);
    }

    @Override
    public ChatDto createGroupChat(UUID adminId, CreateUpdateChatDto dto) {
        List<RelationPerson> members = getPreparedChatMembers(adminId, dto);
        Chat chat = Chat.builder()
                .type(ChatType.GROUP)
                .name(dto.getChatName())
                .adminId(adminId)
                .creationDate(LocalDateTime.now())
                .avatarId(dto.getAvatarId())
                .members(members)
                .build();

        chat = chatRepository.save(chat);
        return chatMapper.entityToDto(chat);
    }

    @Override
    public ChatDto updateGroupChat(UUID adminId, UUID chatId, CreateUpdateChatDto dto) {
        Chat chat = chatRepository
                .findChatByAdminId(adminId)
                .orElseThrow(() -> new NotFoundException("Чат с таким идентификатором и администратором не найден"));

        List<RelationPerson> members = getPreparedChatMembers(adminId, dto);

        chat.setName(dto.getChatName());
        chat.setAvatarId(dto.getAvatarId());
        chat.setMembers(members);

        chat = chatRepository.save(chat);
        return chatMapper.entityToDto(chat);
    }

    private List<RelationPerson> getPreparedChatMembers(UUID adminId, CreateUpdateChatDto dto) {
        List<UUID> memberIds = dto.getMembersId().stream().distinct().collect(Collectors.toList());

        if (memberIds.contains(adminId)) {
            throw new BadRequestException("Список участников не должен содержать идентификатор создателя");
        }

        validateMemberIds(adminId, memberIds);
        memberIds.add(adminId);
        return relationPersonService.createAllRelationPerson(memberIds);
    }

    private void validateMemberIds(UUID personId, List<UUID> memberIds) {
        List<UUID> unfriendPersonIds = new ArrayList<>();
        for (UUID memberId : memberIds) {
            try {
                BooleanDto isFriends = friendIntegrationService.checkIsFriends(new PairPersonIdDto(personId, memberId));
                if (Boolean.FALSE.equals(isFriends.getValue())) {
                    unfriendPersonIds.add(memberId);
                }
            } catch (RestClientException e) {
                log.error("Ошибка во время интеграционного запроса на проверку существования связи дружбы", e);
                throw new InternalException("Ошибка во время интеграционного запроса на " +
                        "проверку существования связи дружбы", e);
            }
        }

        if (!unfriendPersonIds.isEmpty()) {
            throw new BadRequestException("Не все участники чата являются друзьями: " + unfriendPersonIds);
        }
    }

}
