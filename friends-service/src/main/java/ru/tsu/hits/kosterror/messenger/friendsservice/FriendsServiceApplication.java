package ru.tsu.hits.kosterror.messenger.friendsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс микросервиса.
 */
@SpringBootApplication
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
