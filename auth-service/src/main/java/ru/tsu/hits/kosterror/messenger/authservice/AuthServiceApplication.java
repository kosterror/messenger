package ru.tsu.hits.kosterror.messenger.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.tsu.hits.kosterror.messenger.coresecurity.ImportCoreSecurityApplication;

/**
 * Главный класс микросервиса.
 */
@SpringBootApplication
@ImportCoreSecurityApplication
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
