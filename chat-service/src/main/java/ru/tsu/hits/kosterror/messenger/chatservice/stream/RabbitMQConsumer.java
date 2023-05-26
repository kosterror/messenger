package ru.tsu.hits.kosterror.messenger.chatservice.stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tsu.hits.kosterror.messenger.chatservice.service.person.RelationPersonService;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;

import java.util.function.Consumer;

/**
 * Класс с конфигурацией бинов для приема сообщений из брокера сообщений.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumer {

    private final RelationPersonService relationPersonService;

    /**
     * Создает бин, который принимает сообщения для синхронизации информации о пользователе.
     *
     * @return возвращает анонимную функцию, которая занимается обновлением информации о пользователе.
     */
    @Bean
    public Consumer<PersonDto> synchronizePersonDetails() {
        return personDto -> {
            log.info("Сообщение из RabbitMQ в методе 'synchronizePersonDetails': {}'", personDto);
            relationPersonService.synchronizeRelationPerson(personDto);
        };
    }

}
