package ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.synchronize;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.integration.auth.personinfo.IntegrationPersonInfoService;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.Friend;
import ru.tsu.hits.kosterror.messenger.friendsservice.repository.FriendRepository;

import java.util.List;
import java.util.UUID;

/**
 * Класс, реализующий интерфейс {@link SynchronizeFriendsService}.
 */
@Service
@RequiredArgsConstructor
public class SynchronizeFriendsServiceImpl implements SynchronizeFriendsService {

    private final FriendRepository friendRepository;
    private final IntegrationPersonInfoService integrationPersonInfoService;

    @Override
    public void syncFriendFullName(UUID friendId) {
        try {
            PersonDto person = integrationPersonInfoService.getPersonInfo(friendId);
            List<Friend> friends = friendRepository.findAllByMemberId(person.getId());
            friends.forEach(friend -> friend.setMemberFullName(person.getFullName()));

            friendRepository.saveAll(friends);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException("Пользователь не найден");
        } catch (Exception exception) {
            throw new InternalException("Исключение во время выполнения интеграционного запроса", exception);
        }
    }
}
