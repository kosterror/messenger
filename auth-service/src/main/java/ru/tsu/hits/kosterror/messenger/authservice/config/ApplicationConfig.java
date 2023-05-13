package ru.tsu.hits.kosterror.messenger.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.tsu.hits.kosterror.messenger.core.integration.common.CommonIntegrationService;
import ru.tsu.hits.kosterror.messenger.core.integration.common.CommonIntegrationServiceImpl;
import ru.tsu.hits.kosterror.messenger.core.integration.friends.FriendIntegrationService;
import ru.tsu.hits.kosterror.messenger.core.integration.friends.FriendIntegrationServiceImpl;

/**
 * Конфигурационный класс сервиса.
 */
@Configuration
public class ApplicationConfig {

    /**
     * Создает бин PasswordEncoder с использованием алгоритма BCrypt.
     *
     * @return объект PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
        return new FriendIntegrationServiceImpl(commonIntegrationService());
    }

}
