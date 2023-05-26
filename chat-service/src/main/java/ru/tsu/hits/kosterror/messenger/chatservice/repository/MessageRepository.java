package ru.tsu.hits.kosterror.messenger.chatservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Message;

import java.util.UUID;

/**
 * JPA репозиторий для сущности {@link Message}
 */
public interface MessageRepository extends JpaRepository<Message, UUID> {
}
