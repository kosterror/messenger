package ru.tsu.hits.kosterror.messenger.friendsservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import ru.tsu.hits.kosterror.messenger.core.util.HeaderValues;

/**
 * Класс конфигурации для сваггера
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "messenger", version = "v1"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "Bearer"
)
@SecurityScheme(
        name = HeaderValues.HEADER_API_KEY,
        type = SecuritySchemeType.APIKEY,
        scheme = HeaderValues.HEADER_API_KEY,
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}