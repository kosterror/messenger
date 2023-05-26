package ru.tsu.hits.kosterror.messenger.chatservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Параметры фильтрации чатов.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatFilters {

    private String chatName;

}
