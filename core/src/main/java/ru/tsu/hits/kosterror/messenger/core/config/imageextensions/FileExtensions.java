package ru.tsu.hits.kosterror.messenger.core.config.imageextensions;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@ConfigurationProperties("app.file.extensions")
@Data
public class FileExtensions {

    private List<String> imageExtensions;

    @PostConstruct
    private void init() {
        log.info("ImageExtensions: {}", this);
    }

}
