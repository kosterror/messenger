package ru.tsu.hits.kosterror.messenger.friendsservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Класс, представляющий собой заблокированного пользователя
 */
@Entity
@Table(name = "blocked_person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockedPerson extends BaseEntity {

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "member_id")
    private UUID memberId;

    @Column(name = "member_full_name")
    private String memberFullName;

    @Column(name = "date_of_addition")
    private LocalDate addedDate;

    @Column(name = "date_of_deletion")
    private LocalDate deleteDate;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}