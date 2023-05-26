package ru.tsu.hits.kosterror.messenger.chatservice.service.attachment;

import ru.tsu.hits.kosterror.messenger.chatservice.entity.Attachment;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Message;

import java.util.UUID;

/**
 * Интерфейс, предоставляющий методы для взаимодействия с {@link Attachment}.
 */
public interface AttachmentService {

    /**
     * Создает и сохраняет {@link Attachment} в БД.
     *
     * @param message  сущность сообщения, к которому относится вложение.
     * @param fileId   идентификтаор файла из file-storage-service.
     * @param personId идентификатор автора.
     * @return сохраненная сущность {@link Attachment}.
     */
    Attachment saveAttachment(Message message, UUID fileId, UUID personId);

}
