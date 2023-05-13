package ru.tsu.hits.kosterror.messenger.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.tsu.hits.kosterror.messenger.core.requestlogger.ImportRequestLoggingFilter;
import ru.tsu.hits.kosterror.messenger.coresecurity.ImportCoreSecurityApplication;

@SpringBootApplication
@ImportCoreSecurityApplication
@ImportRequestLoggingFilter
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

}
