package ru.tsu.hits.kosterror.messenger.friendsservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
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
public class Friend extends BaseEntity {

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "member_id")
    private UUID memberId;

    @Column(name = "member_full_name")
    private String memberFullName;

    @Column(name = "date_of_addition")
    private LocalDateTime addedDate;

    @Column(name = "date_of_deletion")
    private LocalDateTime deletedDate;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
