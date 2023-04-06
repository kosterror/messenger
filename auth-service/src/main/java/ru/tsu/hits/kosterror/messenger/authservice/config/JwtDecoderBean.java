package ru.tsu.hits.kosterror.messenger.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * Класс для создания бина декодеровщика токенов.
 */
@Configuration
@RequiredArgsConstructor
public class JwtDecoderBean {

    private final RsaKeyProperties rsaKeys;

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                .withPublicKey(rsaKeys.getPublicKey())
                .build();
    }

}
