package ru.tsu.hits.kosterror.messenger.coresecurity.security.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.tsu.hits.kosterror.messenger.coresecurity.config.SecurityProperties;
import ru.tsu.hits.kosterror.messenger.coresecurity.security.authenticationtoken.IntegrationAuthentication;
import ru.tsu.hits.kosterror.messenger.coresecurity.service.errorsender.HttpErrorSender;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.tsu.hits.kosterror.messenger.core.util.HeaderValues.HEADER_API_KEY;

/**
 * Фильтр для проверки интеграционного запроса на основе ключа api-key.
 */
@RequiredArgsConstructor
@Slf4j
public class IntegrationFilter extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;
    private final HttpErrorSender errorSender;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String receivedApiKey = request.getHeader(HEADER_API_KEY);

        log.info("Проверка ключа '{}' для интеграционных запросов. Запрос '{}', '{}'",
                receivedApiKey,
                request.getMethod(),
                request.getRequestURI()
        );
        if (!securityProperties.getIntegration().getApiKey().equals(receivedApiKey)) {
            errorSender.sendError(response, 401, "Невалидный Api-Key");
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(new IntegrationAuthentication());
        filterChain.doFilter(request, response);
    }

}
