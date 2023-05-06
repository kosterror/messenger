package ru.tsu.hits.kosterror.messenger.core.config.imageextensions;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(Extensions.class)
@ConfigurationPropertiesScan("ru.tsu.hits.kosterror.messenger.coresecurity")
@EnableConfigurationProperties
public @interface ImportImageExtensions {
}
