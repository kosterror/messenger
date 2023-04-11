package ru.tsu.hits.kosterror.messenger.friendsservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для фильтрации друзей.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendBasicFilters {

    private String friendFullName;

}
