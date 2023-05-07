package ru.tsu.hits.kosterror.messenger.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.kosterror.messenger.notificationservice.entity.Notification;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Integer countByPersonIdAndIsChecked(UUID personId, boolean isChecked);

}
