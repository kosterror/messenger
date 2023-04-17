package ru.tsu.hits.kosterror.messenger.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Конфигурационный класс для создания бина PasswordEncoder.
 */
@Configuration
public class PasswordEncoderBean {

    /**
     * Создает бин PasswordEncoder с использованием алгоритма BCrypt.
     *
     * @return объект PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
