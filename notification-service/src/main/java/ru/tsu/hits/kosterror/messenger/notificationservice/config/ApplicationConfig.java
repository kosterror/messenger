package ru.tsu.hits.kosterror.messenger.notificationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tsu.hits.kosterror.messenger.core.config.SwaggerConfig;
import ru.tsu.hits.kosterror.messenger.core.controller.ExceptionHandlingController;

/**
 * Класс с конфигурацией для сервиса.
 */
@Configuration
public class ApplicationConfig {

    /**
     * Создает бин, которые будет отлавливать исключения во время HTTP запросов.
     *
     * @return бин {@link ExceptionHandlingController}.
     */
    @Bean
    public ExceptionHandlingController exceptionHandlingController() {
        return new ExceptionHandlingController();
    }

    /**
     * Создает бин с конфигом для сваггера.
     *
     * @return бин {@link ru.tsu.hits.kosterror.messenger.core.config.SwaggerConfig}.
     */
    @Bean
    ru.tsu.hits.kosterror.messenger.core.config.SwaggerConfig swaggerConfig() {
        return new SwaggerConfig();
    }

}
