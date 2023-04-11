package ru.tsu.hits.kosterror.messenger.coresecurity;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Класс для сканирования бинов для контекста спринга.
 */
@ConfigurationPropertiesScan("ru.tsu.hits.kosterror.messenger.coresecurity")
@Configuration
@ComponentScan
public class CoreSecurityConfiguration {

}
