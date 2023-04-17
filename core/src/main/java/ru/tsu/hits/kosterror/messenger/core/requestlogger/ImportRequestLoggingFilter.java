package ru.tsu.hits.kosterror.messenger.core.requestlogger;

import org.springframework.context.annotation.Import;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для включения в проект бина {@link AbstractRequestLoggingFilter}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RequestLoggingFilterConfig.class)
public @interface ImportRequestLoggingFilter {
}
