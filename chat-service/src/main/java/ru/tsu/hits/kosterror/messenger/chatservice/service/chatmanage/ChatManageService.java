package ru.tsu.hits.kosterror.messenger.chatservice.service.chatmanage;

import ru.tsu.hits.kosterror.messenger.chatservice.dto.ChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.CreateUpdateChatDto;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Chat;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.RelationPerson;

import java.util.UUID;

/**
 * Интерфейс предоставляющий методы для создания и изменения сущностей {@link Chat}.
 */
public interface ChatManageService {

    /**
     * Создать личный чат между двумя людьми.
     *
     * @param first  первый пользователь.
     * @param second второй пользователь
     * @return созданный чат.
     */
    Chat createPrivateChat(RelationPerson first, RelationPerson second);

    /**
     * Метод для создания группового чата.
     *
     * @param adminId идентификатор создателя.
     * @param dto     информация о создаваемом чате.
     * @return сохраненная информация о чате.
     */
    ChatDto createGroupChat(UUID adminId, CreateUpdateChatDto dto);

    /**
     * Метод для изменения группового чата.
     *
     * @param adminId это создатель чата он изменяет чат.
     * @param chatId  идентификатор чата.
     * @param dto     новая информация.
     * @return сохраненная информация.
     */
    ChatDto updateGroupChat(UUID adminId, UUID chatId, CreateUpdateChatDto dto);

}
