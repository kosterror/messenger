package ru.tsu.hits.kosterror.messenger.friendsservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для фильтрации заблокированных пользователей.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockedPersonBasicFilters {

    private String friendFullName;

}
