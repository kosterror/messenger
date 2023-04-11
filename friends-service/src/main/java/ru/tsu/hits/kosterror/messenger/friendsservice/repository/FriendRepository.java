package ru.tsu.hits.kosterror.messenger.friendsservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.Friend;

import java.util.UUID;

/**
 * Интерфейс для взаимодействия с сущностями {@link Friend} из БД.
 */
public interface FriendRepository extends JpaRepository<Friend, UUID> {
}
