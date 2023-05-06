package ru.tsu.hits.kosterror.messenger.chatservice.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

/**
 * Класс, представляющий собой сущность вложения для сообщения.
 */
@Entity
@Table(name = "attachment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    @Column(name = "name")
    private String name;

    @Column(name = "file_id")
    private UUID fileId;

}
