package ru.tsu.hits.kosterror.messenger.coresecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Главный класс сервиса.
 */
@ConfigurationPropertiesScan("ru.tsu.hits.kosterror.messenger.coresecurity")
@SpringBootApplication
public class CoreSecurityApplication {

    /**
     * Главный метод сервиса.
     *
     * @param args параметры.
     */
    public static void main(String[] args) {
        SpringApplication.run(CoreSecurityApplication.class, args);
    }

}
