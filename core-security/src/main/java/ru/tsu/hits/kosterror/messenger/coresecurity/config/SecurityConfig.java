package ru.tsu.hits.kosterror.messenger.coresecurity.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.tsu.hits.kosterror.messenger.coresecurity.security.filter.IntegrationFilter;
import ru.tsu.hits.kosterror.messenger.coresecurity.security.filter.JwtFilter;
import ru.tsu.hits.kosterror.messenger.coresecurity.service.errorsender.HttpErrorSender;
import ru.tsu.hits.kosterror.messenger.coresecurity.service.jwt.decoder.JwtDecoderService;

import java.util.Arrays;
import java.util.Objects;

/**
 * Конфигурация security проекта.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityProperties properties;
    private final HttpErrorSender errorSender;
    private final JwtDecoderService jwtDecoder;

    @SneakyThrows
    @Bean
    public SecurityFilterChain filterChainJwt(HttpSecurity http) {
        http = http
                .addFilterBefore(buildJwtFilter(), UsernamePasswordAuthenticationFilter.class)
                .requestMatcher(
                        filterPredicate(
                                properties.getJwtToken().getRootPath(),
                                properties.getJwtToken().getPermitAllEndpoints()
                        )
                );

        return finishConfiguration(http);
    }

    @SneakyThrows
    @Bean
    public SecurityFilterChain filterChainIntegration(HttpSecurity http) {
        http
                .requestMatcher(
                        filterPredicate(
                                properties.getIntegration().getRootPath()
                        )
                )
                .addFilterBefore(
                        buildIntegrationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

        return finishConfiguration(http);
    }

    @SneakyThrows
    public SecurityFilterChain finishConfiguration(HttpSecurity http) {
        return http
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .build();
    }

    private RequestMatcher filterPredicate(String rootPath, String... ignore) {
        return request -> Objects.nonNull(request.getServletPath())
                && request.getServletPath().startsWith(rootPath)
                && Arrays.stream(ignore).noneMatch(item -> request.getServletPath().startsWith(item));
    }

    private JwtFilter buildJwtFilter() {
        return new JwtFilter(jwtDecoder, errorSender);
    }

    private IntegrationFilter buildIntegrationFilter() {
        return new IntegrationFilter(properties, errorSender);
    }

}
