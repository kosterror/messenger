package ru.tsu.hits.kosterror.messenger.friendsservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.Friend;

import java.util.UUID;

/**
 * Интерфейс для взаимодействия с сущностями {@link Friend} из БД.
 */
public interface FriendRepository extends JpaRepository<Friend, UUID> {

    /**
     * Метод для получения списка друзей, который в своем ФИО имеют подстроку {@code memberFullName}
     * без учёта регистра.
     *
     * @param ownerId        идентификатор пользователя, чьих друзей ищем.
     * @param memberFullName часть ФИО.
     * @param isDeleted      удален ли друг.
     * @param pageable       параметры пагинации.
     * @return список друзей, обернутый в {@link Page}.
     */
    Page<Friend> getFriendsByOwnerIdAndMemberFullNameContainingIgnoreCaseAndIsDeleted(UUID ownerId,
                                                                                      String memberFullName,
                                                                                      boolean isDeleted,
                                                                                      Pageable pageable
    );

    /**
     * Метод для получения списка друзей.
     *
     * @param ownerId   идентификатор пользователя, чьих друзей ищем.
     * @param isDeleted удален ли друг.
     * @param pageable  параметры пагинации.
     * @return список друзей, обернутый в {@link Page}.
     */
    Page<Friend> getFriendsByOwnerIdAndIsDeleted(UUID ownerId, Boolean isDeleted, Pageable pageable);

}
