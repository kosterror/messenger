package ru.tsu.hits.kosterror.messenger.chatservice.service.message;

import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.MessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.SendMessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Message;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс, предоставляющий методы для взаимодействия с сущностью {@link Message}.
 */
public interface MessageService {

    /**
     * Отправляет метод в чат.
     *
     * @param authorId идентификатор автора.
     * @param dto      информация об отправляемом сообщении.
     */
    @Transactional
    void sendMessageToChat(UUID authorId, SendMessageDto dto);

    /**
     * Отправляет личное сообщение пользователю.
     *
     * @param authorId id автора.
     * @param dto      информация об отправляемом сообщении.
     */
    @Transactional
    void sendMessageToPerson(UUID authorId, SendMessageDto dto);

    /**
     * Получает список сообщений в чате.
     *
     * @param personId id того, кто пытается получить список сообщений.
     * @param chatId   id чата.
     * @return список сообщений.
     */
    List<MessageDto> getChatMessages(UUID personId, UUID chatId);

    /**
     * Ищет сообщения с подстрокой в тексте сообщения или названиях вложений.
     *
     * @param personId  id того, кто ищет.
     * @param substring подстрока.
     * @return список подходящих сообщений.
     */
    List<MessageDto> findMessages(UUID personId, String substring);

}
