package ru.tsu.hits.kosterror.messenger.filestorageservice.config;

import io.minio.MinioClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@ConfigurationProperties("minio")
public class MinioConfig {

    private String url;
    private String accessKey;
    private String secretKey;
    private String bucket;

    @PostConstruct
    private void init() {
        log.info("MinioConfig: {}", this);
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient
                .builder()
                .credentials(accessKey, secretKey)
                .endpoint(url)
                .build();
    }

}
