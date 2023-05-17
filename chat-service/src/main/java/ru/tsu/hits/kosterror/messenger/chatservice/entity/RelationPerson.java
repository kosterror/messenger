package ru.tsu.hits.kosterror.messenger.chatservice.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * Класс, представляющий собой эмуляцию сущности пользователя.
 */
@Entity
@Table(name = "relation_person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelationPerson extends BaseEntity {

    @Column(name = "person_id")
    private UUID personId;

    @Column(name = "full_name")
    private String fullName;

    @ManyToMany(mappedBy = "members")
    private List<Chat> chats;

    @OneToMany(mappedBy = "relationPerson")
    private List<Message> messages;

}
