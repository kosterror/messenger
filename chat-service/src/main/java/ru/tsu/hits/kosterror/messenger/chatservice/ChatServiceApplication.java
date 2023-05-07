package ru.tsu.hits.kosterror.messenger.chatservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.tsu.hits.kosterror.messenger.core.config.imageextensions.ImportImageExtensions;
import ru.tsu.hits.kosterror.messenger.coresecurity.ImportCoreSecurityApplication;

/**
 * Класс с точкой входа в программу.
 */
@SpringBootApplication
@ImportCoreSecurityApplication
@ImportImageExtensions
public class ChatServiceApplication {

    /**
     * Метод, который является точкой входа в программу.
     *
     * @param args параметры для запуска.
     */
    public static void main(String[] args) {
        SpringApplication.run(ChatServiceApplication.class, args);
    }

}
