package ru.tsu.hits.kosterror.messenger.friendsservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.BlockedPerson;

import java.util.UUID;

/**
 * Интерфейс для взаимодействия с сущностями {@link BlockedPerson} из БД.
 */
@Repository
public interface BlockedPersonRepository extends JpaRepository<BlockedPerson, UUID> {

    /**
     * Метод для получения списка заблокированных пользователей, которые в своем
     * ФИО имеют подстроку {@code memberFullName} без учёта регистра.
     *
     * @param ownerId        идентификатор пользователя.
     * @param memberFullName часть ФИО.
     * @param isDeleted      удален ли пользователь из черного списка.
     * @param pageable       параметры пагинации.
     * @return список заблокированных пользователей, обернутый в {@link Page}.
     */
    Page<BlockedPerson> getBlockedPersonsByOwnerIdAndMemberFullNameContainingIgnoreCaseAndIsDeleted
    (
            UUID ownerId,
            String memberFullName,
            boolean isDeleted,
            Pageable pageable
    );

    /**
     * Метод для получения списка заблокированных пользователей.
     *
     * @param ownerId   идентификатор пользователя.
     * @param isDeleted удален ли пользователь из черного списка.
     * @param pageable  параметры пагинации.
     * @return список заблокированных пользователей, обернутый в {@link Page}.
     */
    Page<BlockedPerson> getBlockedPersonsByOwnerIdAndIsDeleted(UUID ownerId, Boolean isDeleted, Pageable pageable);

}
