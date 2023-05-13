package ru.tsu.hits.kosterror.messenger.friendsservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tsu.hits.kosterror.messenger.core.integration.auth.personinfo.PersonInfoService;
import ru.tsu.hits.kosterror.messenger.core.integration.auth.personinfo.PersonInfoServiceImpl;
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
     * Метод для создания бина {@link PersonInfoService}.
     *
     * @return бин {@link PersonInfoService}.
     */
    @Bean
    public PersonInfoService personInfoService() {
        return new PersonInfoServiceImpl(commonIntegrationService());
    }

}
