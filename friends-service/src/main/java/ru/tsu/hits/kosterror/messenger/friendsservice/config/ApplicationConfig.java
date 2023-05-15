package ru.tsu.hits.kosterror.messenger.friendsservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tsu.hits.kosterror.messenger.core.config.SwaggerConfig;
import ru.tsu.hits.kosterror.messenger.core.controller.ExceptionHandlingController;
import ru.tsu.hits.kosterror.messenger.core.integration.auth.personinfo.IntegrationPersonInfoService;
import ru.tsu.hits.kosterror.messenger.core.integration.auth.personinfo.IntegrationPersonInfoServiceImpl;
import ru.tsu.hits.kosterror.messenger.core.integration.common.CommonIntegrationService;
import ru.tsu.hits.kosterror.messenger.core.integration.common.CommonIntegrationServiceImpl;

/**
 * Класс с конфигурацией для сервиса.
 */
@Configuration
public class ApplicationConfig {

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
     * Метод для создания бина {@link IntegrationPersonInfoService}.
     *
     * @return бин {@link IntegrationPersonInfoService}.
     */
    @Bean
    public IntegrationPersonInfoService personInfoService() {
        return new IntegrationPersonInfoServiceImpl(commonIntegrationService());
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

}
