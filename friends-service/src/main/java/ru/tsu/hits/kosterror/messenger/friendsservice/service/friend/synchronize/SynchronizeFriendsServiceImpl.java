package ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.synchronize;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.core.response.ApiError;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.Friend;
import ru.tsu.hits.kosterror.messenger.friendsservice.repository.FriendRepository;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.integration.authservice.AuthIntegrationService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SynchronizeFriendsServiceImpl implements SynchronizeFriendsService {

    private final FriendRepository friendRepository;
    private final AuthIntegrationService authIntegrationService;
    private final ObjectMapper objectMapper;

    @Override
    public void syncFriendFullName(UUID friendId) {
        ResponseEntity<Object> response = authIntegrationService.getPersonInfo(friendId);

        if (response.getStatusCode() == HttpStatus.OK) {
            PersonDto person = objectMapper.convertValue(response.getBody(), PersonDto.class);
            List<Friend> friends = friendRepository.findAllByMemberId(person.getId());
            friends.forEach(friend -> friend.setMemberFullName(person.getFullName()));

            friendRepository.saveAll(friends);
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new BadRequestException(
                    String.format("Пользователь с идентификатором '%s' не существует", friendId)
            );
        } else {
            ApiError error = objectMapper.convertValue(response.getBody(), ApiError.class);
            throw new InternalException(String.format("Ошибка во время выполнения интеграционного запроса:" +
                    "текст ошибки: '%s', статус код: '%s'", error.getMessage(), error.getStatus()));
        }
    }
}
