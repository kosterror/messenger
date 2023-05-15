package ru.tsu.hits.kosterror.messenger.chatservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.tsu.hits.kosterror.messenger.core.config.SwaggerConfig;
import ru.tsu.hits.kosterror.messenger.core.controller.ExceptionHandlingController;
import ru.tsu.hits.kosterror.messenger.core.integration.common.CommonIntegrationService;
import ru.tsu.hits.kosterror.messenger.core.integration.common.CommonIntegrationServiceImpl;
import ru.tsu.hits.kosterror.messenger.core.integration.filestorage.FileStorageIntegrationIntegrationServiceImpl;
import ru.tsu.hits.kosterror.messenger.core.integration.filestorage.FileStorageIntegrationService;
import ru.tsu.hits.kosterror.messenger.core.integration.friends.FriendIntegrationService;
import ru.tsu.hits.kosterror.messenger.core.integration.friends.FriendIntegrationServiceImpl;

/**
 * Конфигурационный класс сервиса.
 */
@Configuration
public class ApplicationConfig {

    /**
     * Создает бин {@link RestTemplate}.
     *
     * @return бин {@link RestTemplate}.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Метод для создания бина {@link CommonIntegrationService}.
     *
     * @return бин {@link CommonIntegrationService}.
     */
    @Bean
    public CommonIntegrationService commonIntegrationService() {
        return new CommonIntegrationServiceImpl();
    }

    /**
     * Метод для создания бина {@link FriendIntegrationService}.
     *
     * @return бин {@link CommonIntegrationService}.
     */
    @Bean
    public FriendIntegrationService friendIntegrationService() {
        return new FriendIntegrationServiceImpl(restTemplate(), commonIntegrationService());
    }

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
     * @return бин {@link SwaggerConfig}.
     */
    @Bean
    SwaggerConfig swaggerConfig() {
        return new SwaggerConfig();
    }

    @Bean
    public FileStorageIntegrationService fileStorageIntegrationService() {
        return new FileStorageIntegrationIntegrationServiceImpl(commonIntegrationService());
    }

}
