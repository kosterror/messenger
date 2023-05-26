package ru.tsu.hits.kosterror.messenger.chatservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Attachment;

import java.util.UUID;

/**
 * JPA репозиторий для сущности Attachment.
 */
public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
}
