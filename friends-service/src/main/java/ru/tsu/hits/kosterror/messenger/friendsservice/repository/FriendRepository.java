package ru.tsu.hits.kosterror.messenger.friendsservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.Friend;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс для взаимодействия с сущностями {@link Friend} из БД.
 */
public interface FriendRepository extends JpaRepository<Friend, UUID> {

    /**
     * Метод для получения сущности друга по обоим участникам.
     *
     * @param ownerId  владелец дружбы.
     * @param memberId подписчик дружбы.
     * @return сущность друга.
     */
    Optional<Friend> findFriendByOwnerIdAndMemberId(UUID ownerId, UUID memberId);

    /**
     * Метод для получения сущности друга по идентификаторам участников и статусу дружбы.
     *
     * @param ownerId   владелец дружбы.
     * @param memberId  подписчик дружбы.
     * @param isDeleted статус дружбы.
     * @return сущность друга.
     */
    Optional<Friend> findFriendByOwnerIdAndMemberIdAndIsDeleted(UUID ownerId, UUID memberId, boolean isDeleted);

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

    /**
     * Метод для проверки существования друга с заданными параметрами.
     *
     * @param ownerId   целевой пользователь.
     * @param memberId  внешний пользователь.
     * @param isDeleted удален ли друг.
     * @return информацию о существовании такой записи.
     */
    boolean existsByOwnerIdAndMemberIdAndIsDeleted(UUID ownerId, UUID memberId, boolean isDeleted);

    /**
     * Метод для поиска всех друзей по идентификатору внешнего пользователя.
     *
     * @param memberId идентификатор внешнего пользователя.
     * @return список друзей с заданным идентификатором внешнего пользователя.
     */
    List<Friend> findAllByMemberId(UUID memberId);

}
