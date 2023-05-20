package ru.tsu.hits.kosterror.messenger.friendsservice.stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.synchronize.SynchronizeBlockedPersonsService;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.synchronize.SynchronizeFriendsService;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumer {

    private final SynchronizeBlockedPersonsService synchronizeBlockedPersonsService;
    private final SynchronizeFriendsService synchronizeFriendsService;

    @Bean
    public Consumer<PersonDto> synchronizePersonDetails() {
        return personDto -> {
            log.info("Сообщение из RabbitMQ в методе 'synchronizePersonDetails': {}'", personDto);
            synchronizeBlockedPersonsService.syncBlockedPerson(personDto);
            synchronizeFriendsService.syncFriend(personDto);
        };
    }

}
