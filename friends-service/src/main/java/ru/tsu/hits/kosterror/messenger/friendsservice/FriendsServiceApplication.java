package ru.tsu.hits.kosterror.messenger.friendsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.tsu.hits.kosterror.messenger.core.requestlogger.ImportRequestLoggingFilter;
import ru.tsu.hits.kosterror.messenger.coresecurity.ImportCoreSecurityApplication;

/**
 * Главный класс микросервиса.
 */
@SpringBootApplication
@ImportCoreSecurityApplication
@ImportRequestLoggingFilter
public class FriendsServiceApplication {

    /**
     * Главный метод.
     *
     * @param args параметры.
     */
    public static void main(String[] args) {
        SpringApplication.run(FriendsServiceApplication.class, args);
    }

}
