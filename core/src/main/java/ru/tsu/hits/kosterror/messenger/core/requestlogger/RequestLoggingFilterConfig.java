package ru.tsu.hits.kosterror.messenger.core.requestlogger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.filter.ServletContextRequestLoggingFilter;

/**
 * Класс с созданием бина для {@link AbstractRequestLoggingFilter}.
 */
@Configuration
@ComponentScan
public class RequestLoggingFilterConfig {

    @Value("${app.logging-requests.include-client-info}")
    private boolean includeClientInfo;

    @Value("${app.logging-requests.include-query-string}")
    private boolean includeQueryString;

    @Value("${app.logging-requests.include-payload}")
    private boolean includePayload;

    @Value("${app.logging-requests.include-headers}")
    private boolean includeHeaders;

    @Value("${app.logging-requests.max-payload-length}")
    private int maxPayloadLength;

    /**
     * Метод с созданием бина.
     *
     * @return бин {@link AbstractRequestLoggingFilter}.
     */
    @Bean
    AbstractRequestLoggingFilter abstractRequestLoggingFilter() {
        ServletContextRequestLoggingFilter loggingFilter = new ServletContextRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(includeClientInfo);
        loggingFilter.setIncludeQueryString(includeQueryString);
        loggingFilter.setIncludePayload(includePayload);
        loggingFilter.setIncludeHeaders(includeHeaders);
        loggingFilter.setMaxPayloadLength(maxPayloadLength);

        return loggingFilter;
    }

}
