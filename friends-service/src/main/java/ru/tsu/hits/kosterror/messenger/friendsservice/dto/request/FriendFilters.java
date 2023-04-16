package ru.tsu.hits.kosterror.messenger.friendsservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO для фильтрации друзей.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendFilters {

    private String memberFullName;

    private LocalDate addedDate;

}
