package ru.tsu.hits.kosterror.messenger.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.kosterror.messenger.notificationservice.entity.Notification;

import java.util.UUID;

/**
 * JPA репозиторий для {@link Notification}.
 */
@Repository
public interface NotificationRepository
        extends JpaRepository<Notification, UUID>, JpaSpecificationExecutor<Notification> {

    Integer countByPersonIdAndIsChecked(UUID personId, boolean isChecked);

}
