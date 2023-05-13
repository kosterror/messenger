package ru.tsu.hits.kosterror.messenger.notificationservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.tsu.hits.kosterror.messenger.core.enumeration.NotificationType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Класс, который представляет собой сущность уведомления.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {

    @Column(name = "person_id")
    private UUID personId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private NotificationType type;

    @Column(name = "message")
    private String message;

    @Column(name = "received_date")
    private LocalDateTime receivedDate;

    @Column(name = "is_checked")
    private Boolean isChecked;

    @Column(name = "checked_date")
    private LocalDateTime checkedDate;

}
