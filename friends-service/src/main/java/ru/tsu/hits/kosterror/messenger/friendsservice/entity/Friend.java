package ru.tsu.hits.kosterror.messenger.friendsservice.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Класс, представляющий собой сущность друга.
 */
@Entity
@Table(name = "friend")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friend extends BaseEntity {

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "member_id")
    private UUID memberId;

    @Column(name = "member_full_name")
    private String memberFullName;

    @Column(name = "date_of_addition")
    private LocalDate addedDate;

    @Column(name = "date_of_deletion")
    private LocalDate deletedDate;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
