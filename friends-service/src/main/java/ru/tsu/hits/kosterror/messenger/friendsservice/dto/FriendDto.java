package ru.tsu.hits.kosterror.messenger.friendsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.Friend;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Класс, представляющий собой DTO для {@link Friend}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendDto {

    private UUID id;
    private LocalDate addedDate;
    private UUID ownerId;
    private UUID memberId;
    private String memberFullName;

}
