package ru.tsu.hits.kosterror.messenger.chatservice.stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tsu.hits.kosterror.messenger.chatservice.service.person.RelationPersonService;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumer {

    private final RelationPersonService relationPersonService;

    @Bean
    public Consumer<PersonDto> synchronizePersonDetails() {
        return personDto -> {
            log.info("Сообщение из RabbitMQ в методе 'synchronizePersonDetails': {}'", personDto);
            relationPersonService.synchronizeRelationPerson(personDto);
        };
    }

}
