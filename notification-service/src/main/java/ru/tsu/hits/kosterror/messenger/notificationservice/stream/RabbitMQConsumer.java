package ru.tsu.hits.kosterror.messenger.notificationservice.stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tsu.hits.kosterror.messenger.core.dto.NewNotificationDto;
import ru.tsu.hits.kosterror.messenger.notificationservice.service.notificatiomanage.NotificationManageService;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumer {

    private final NotificationManageService notificationManageService;

    @Bean
    public Consumer<NewNotificationDto> createNotification() {
        return newNotificationDto -> {
            log.info("Сообщение из RabbitMQ в методе 'createNotification': {}", newNotificationDto);
            notificationManageService.createNotification(newNotificationDto);
        };
    }

}
