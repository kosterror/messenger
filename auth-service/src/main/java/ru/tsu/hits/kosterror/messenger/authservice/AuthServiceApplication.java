package ru.tsu.hits.kosterror.messenger.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.tsu.hits.kosterror.messenger.authservice.config.RsaKeyProperties;

/**
 * Главный класс микросервиса.
 */
@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class AuthServiceApplication {

    /**
     * Главный метод.
     *
     * @param args параметры.
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

}
