package ru.tsu.hits.kosterror.messenger.filestorageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import ru.tsu.hits.kosterror.messenger.core.requestlogger.ImportRequestLoggingFilter;
import ru.tsu.hits.kosterror.messenger.coresecurity.ImportCoreSecurityApplication;

@ConfigurationPropertiesScan("ru.tsu.hits.kosterror.messenger.filestorageservice")
@SpringBootApplication
@ImportCoreSecurityApplication
@ImportRequestLoggingFilter
public class FileStorageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileStorageServiceApplication.class, args);
    }

}
