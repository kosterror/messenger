package ru.tsu.hits.kosterror.messenger.chatservice.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Класс, представляющий собой сущность сообщения.
 */
@Entity
@Table(name = "message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Message extends BaseEntity {

    @Column(name = "text")
    private String text;

    @Column(name = "date_of_sending")
    private LocalDateTime sendingDate;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private List<Attachment> attachments;

    @ManyToOne
    @JoinColumn(name = "relation_person_id")
    private RelationPerson relationPerson;

}
