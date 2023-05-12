package ru.tsu.hits.kosterror.messenger.notificationservice.service.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.tsu.hits.kosterror.messenger.notificationservice.entity.Notification;
import ru.tsu.hits.kosterror.messenger.notificationservice.entity.Notification_;
import ru.tsu.hits.kosterror.messenger.notificationservice.enumeration.NotificationType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class NotificationSpecification {

    public static Specification<Notification> personId(UUID id) {
        return (notification, query, builder) -> builder.equal(notification.get(Notification_.PERSON_ID), id);
    }

    public static Specification<Notification> messageContainsSubstring(String substring) {
        return ((notification, query, builder) ->
                builder.like(
                        builder.upper(notification.get(Notification_.MESSAGE)),
                        "%" + substring.toUpperCase() + "%")
        );
    }

    public static Specification<Notification> receivedDateTimeBefore(LocalDateTime dateTime) {
        return (notification, query, builder) ->
                builder.lessThanOrEqualTo(notification.get(Notification_.RECEIVED_DATE), dateTime);
    }

    public static Specification<Notification> receivedDateTimeAfter(LocalDateTime dateTime) {
        return (notification, query, builder) ->
                builder.greaterThanOrEqualTo(notification.get(Notification_.RECEIVED_DATE), dateTime);
    }

    public static Specification<Notification> typeIn(List<NotificationType> types) {
        return ((notification, query, builder) -> builder.in(notification.get(Notification_.TYPE)).value(types));
    }

}
