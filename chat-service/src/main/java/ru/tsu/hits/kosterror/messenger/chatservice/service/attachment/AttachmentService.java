package ru.tsu.hits.kosterror.messenger.chatservice.service.attachment;

import ru.tsu.hits.kosterror.messenger.chatservice.entity.Attachment;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Message;

import java.util.UUID;

public interface AttachmentService {

    Attachment saveAttachment(Message message, UUID fileId, UUID personId);

}
