package ru.tsu.hits.kosterror.messenger.friendsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.BlockedPerson;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Класс, представляющий собой DTO для {@link BlockedPerson}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockedPersonDto {

    private UUID id;
    private LocalDateTime addedDate;
    private UUID ownerId;
    private UUID memberId;
    private String memberFullName;


}
