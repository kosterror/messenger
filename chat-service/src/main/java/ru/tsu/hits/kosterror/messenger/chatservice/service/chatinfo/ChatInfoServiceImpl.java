package ru.tsu.hits.kosterror.messenger.chatservice.service.chatinfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatFilters;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatMessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Chat;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Message;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.RelationPerson;
import ru.tsu.hits.kosterror.messenger.chatservice.enumeration.ChatType;
import ru.tsu.hits.kosterror.messenger.chatservice.mapper.ChatMapper;
import ru.tsu.hits.kosterror.messenger.chatservice.repository.ChatRepository;
import ru.tsu.hits.kosterror.messenger.chatservice.repository.RelationPersonRepository;
import ru.tsu.hits.kosterror.messenger.chatservice.service.person.RelationPersonService;
import ru.tsu.hits.kosterror.messenger.core.exception.ForbiddenException;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingParamsResponse;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatInfoServiceImpl implements ChatInfoService {

    private static final String NO_ACCESS_TO_THE_CHAT_MESSAGE = "Нет доступа к чату";
    private static final String CHAT_NOT_FOUND = "Чат с таким идентификатором не найден";
    private static final int DEFAULT_PAGE_SIZE = 50;
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final RelationPersonRepository relationPersonRepository;
    private final RelationPersonService relationPersonService;

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

    @Override
    public PagingResponse<List<ChatMessageDto>> getChats(UUID personId,
                                                         PagingFilteringRequest<ChatFilters> request) {
        RelationPerson sourceRelationPerson = relationPersonRepository
                .findByPersonId(personId)
                .orElseGet(() -> relationPersonService.createRelationPersonEntity(personId));

        List<Chat> chatPersons = sourceRelationPerson.getChats();

        int pageNumber = request.getPaging().getPage() == null ? DEFAULT_PAGE_NUMBER : request.getPaging().getPage();
        int size = request.getPaging().getSize() == null ? DEFAULT_PAGE_SIZE : request.getPaging().getSize();
        int totalElements = chatPersons.size();
        int totalPage = totalElements % size == 0 ? totalElements / size : totalElements / size + 1;

        PagingParamsResponse pagingParams = new PagingParamsResponse(totalPage, totalElements, pageNumber, size);

        List<ChatMessageDto> chatMessages = chatPersons
                .stream()
                .map(chat -> mapChatToChatMessage(sourceRelationPerson, chat))
                .collect(Collectors.toList());

        chatMessages = filterChatByNameWithIgnoreCase(chatMessages, request.getFilters().getChatName());

        sortChatMessagesByLastMessage(chatMessages);
        List<ChatMessageDto> page = getPage(chatMessages, pagingParams.getPage(), pagingParams.getSize());

        return new PagingResponse<>(pagingParams, page);
    }

    private List<ChatMessageDto> getPage(List<ChatMessageDto> list, int page, int pageSize) {
        int start = pageSize * page;
        int finish = pageSize * (page + 1);
        finish = Math.min(finish, list.size());

        if (start > list.size() - 1) {
            return Collections.emptyList();
        }

        return list.subList(start, finish);
    }

    private ChatMessageDto mapChatToChatMessage(RelationPerson sourceRelationPerson, Chat chat) {
        Optional<Message> lastMessageOpt = getLastMessage(chat);

        String lastMessageText = null;
        boolean isHasAttachment = false;
        LocalDateTime lastMessageDateTime = null;
        UUID lastMessageAuthor = null;

        if (lastMessageOpt.isPresent()) {
            Message lastMessage = lastMessageOpt.get();
            lastMessageText = lastMessage.getText();
            isHasAttachment = !lastMessage.getAttachments().isEmpty();
            lastMessageDateTime = lastMessage.getSendingDate();
            lastMessageAuthor = lastMessage.getRelationPerson().getPersonId();
        }

        return new ChatMessageDto(
                chat.getId(),
                getChatName(sourceRelationPerson, chat),
                lastMessageText,
                isHasAttachment,
                lastMessageDateTime,
                lastMessageAuthor,
                chat.getCreationDate()
        );
    }

    private String getChatName(RelationPerson sourceRelationPerson, Chat chat) {
        if (chat.getType() == ChatType.GROUP) {
            return chat.getName();
        }

        if (chat.getMembers().size() != 2) {
            throw new InternalException("Аномалия в данных. В личном диалоге состоит не два человека");
        }

        return chat
                .getMembers()
                .stream()
                .filter(member -> !member.getPersonId().equals(sourceRelationPerson.getPersonId()))
                .findFirst()
                .orElse(sourceRelationPerson)
                .getFullName();
    }

    private List<ChatMessageDto> filterChatByNameWithIgnoreCase(@NotNull List<ChatMessageDto> chatMessages,
                                                                @Nullable String chatName) {
        if (chatName == null) {
            return new ArrayList<>(chatMessages);
        }

        return chatMessages
                .stream()
                .filter(chat -> (chat.getName().toUpperCase().contains(chatName.toUpperCase())))
                .collect(Collectors.toList());
    }

    private Optional<Message> getLastMessage(Chat chat) {
        List<Message> messages = chat.getMessages();
        if (messages.isEmpty()) {
            return Optional.empty();
        }

        Message lastMessage = messages.get(0);
        for (int i = 1; i < messages.size(); i++) {
            if (messages.get(i).getSendingDate().isAfter(lastMessage.getSendingDate())) {
                lastMessage = messages.get(i);
            }
        }

        return Optional.of(lastMessage);
    }

    private void sortChatMessagesByLastMessage(List<ChatMessageDto> chatMessages) {
        chatMessages.sort((chat1, chat2) -> {
            if (chat1 == null || chat2 == null) {
                throw new NullPointerException("Нельзя отсортировать список чатов, где какие-то элементы равны null");
            }

            LocalDateTime dateTime1 = chat1.getLastMessageDateTime() != null ?
                    chat1.getLastMessageDateTime() : chat1.getCreationDateTime();
            LocalDateTime dateTime2 = chat2.getLastMessageDateTime() != null ?
                    chat2.getLastMessageDateTime() : chat2.getCreationDateTime();

            if (dateTime1.isAfter(dateTime2)) {
                return -1;
            } else if (dateTime1.isBefore(dateTime2)) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    private boolean hasAccessToChat(UUID personId, Chat chat) {
        return chat.getMembers()
                .stream()
                .anyMatch(member -> member.getPersonId().equals(personId));
    }

}
