package ru.tsu.hits.kosterror.messenger.filestorageservice.config;

import com.ibm.icu.text.Transliterator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tsu.hits.kosterror.messenger.core.config.SwaggerConfig;
import ru.tsu.hits.kosterror.messenger.core.controller.ExceptionHandlingController;

/**
 * Класс с конфигурацией сервиса.
 */
@Configuration
public class ApplicationConfig {

    private static final String CYRILLIC_TO_LATIN = "Russian-Latin/BGN";

    /**
     * Создает бин {@link Transliterator}.
     *
     * @return бин {@link Transliterator}.
     */
    @Bean
    Transliterator cyrillicTransliterator() {
        return Transliterator.getInstance(CYRILLIC_TO_LATIN);
    }

    /**
     * Создает бин, которые будет отлавливать исключения во время HTTP запросов.
     *
     * @return бин {@link ExceptionHandlingController}.
     */
    @Bean
    public ExceptionHandlingController exceptionHandlingController() {
        return new ExceptionHandlingController();
    }

    /**
     * Создает бин с конфигом для сваггера.
     *
     * @return бин {@link SwaggerConfig}.
     */
    @Bean
    SwaggerConfig swaggerConfig() {
        return new SwaggerConfig();
    }

}
