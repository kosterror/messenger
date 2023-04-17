package ru.tsu.hits.kosterror.messenger.authservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import ru.tsu.hits.kosterror.messenger.coresecurity.util.Constants;

/**
 * Класс конфигурации для свагера, который позволяет использовать в нем возможность
 * добавления токена в заголовки запросов.
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
        name = Constants.HEADER_API_KEY,
        type = SecuritySchemeType.APIKEY,
        scheme = Constants.HEADER_API_KEY,
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
}