package ru.tsu.hits.kosterror.messenger.chatservice.entity;

import lombok.*;
import ru.tsu.hits.kosterror.messenger.chatservice.enumeration.ChatType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Класс, представляющий собой сущность чата в БД.
 */
@Entity
@Table(name = "chat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat extends BaseEntity {

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private ChatType type;

    @Column(name = "chat_name")
    private String chatName;

    @Column(name = "admin_id")
    private UUID adminId;

    @Column(name = "date_of_creation")
    private LocalDate createdDate;

    @Column(name = "avatar_id")
    private UUID avatarId;

}
