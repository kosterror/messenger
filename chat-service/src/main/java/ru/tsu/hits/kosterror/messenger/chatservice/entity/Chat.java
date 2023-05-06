package ru.tsu.hits.kosterror.messenger.chatservice.entity;

import lombok.*;
import ru.tsu.hits.kosterror.messenger.chatservice.enumeration.ChatType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Класс, представляющий собой сущность чата.
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
    @Enumerated(EnumType.STRING)
    private ChatType type;

    @Column(name = "name")
    private String name;

    @Column(name = "admin_id")
    private UUID adminId;

    @Column(name = "date_of_creation")
    private LocalDateTime creationDate;

    @Column(name = "avatar_id")
    private UUID avatarId;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages;

    @ManyToMany
    @JoinTable(
            name = "chat_person",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "relation_person_id")
    )
    private List<RelationPerson> members;

}
