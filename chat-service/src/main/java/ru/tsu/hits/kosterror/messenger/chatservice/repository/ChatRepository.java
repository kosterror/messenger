package ru.tsu.hits.kosterror.messenger.chatservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Chat;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {

    Optional<Chat> findChatByIdAndAdminId(UUID chatId, UUID adminId);

}
