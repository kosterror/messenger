package ru.tsu.hits.kosterror.messenger.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.tsu.hits.kosterror.messenger.authservice.service.servlet.ServletResponseService;
import ru.tsu.hits.kosterror.messenger.authservice.util.constant.HeaderKeys;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр для добавления тела в те ответы, где статус код равен 401 или 403. Так сделано потому, что
 * нельзя полностью контролировать тело ответа при неуспешной аутентификации в OAuth2, даже с добавлением
 * {@link AuthenticationEntryPoint} и {@link AccessDeniedHandler}, т.к. при верификации токена
 * в OAuth2 не во всех случаях выбрасывались исключения {@link AuthenticationException} и
 * {@link AccessDeniedException}, а просто назначался статус код в ответе.
 */
@Component
@RequiredArgsConstructor
public class FilterChainExceptionHandler extends OncePerRequestFilter {

    private static final String UNAUTHORIZED_MESSAGE = "Не авторизован";
    private static final String FORBIDDEN_MESSAGE = "Доступ запрещен";
    private static final String INTERNAL_ERROR_MESSAGE = "Непредвиденная внутренняя ошибка сервера";
    private final ServletResponseService servletResponseService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ServletException e) {
            servletResponseService.sendError(response, 500, INTERNAL_ERROR_MESSAGE);
        } finally {
            logger.info("размер: " + response.getBufferSize());

            if (!response.containsHeader(HeaderKeys.HANDLED_EXCEPTION)) {
                if (response.getStatus() == 401) {

                    servletResponseService.sendError(response, 401, UNAUTHORIZED_MESSAGE);
                } else if (response.getStatus() == 403) {
                    servletResponseService.sendError(response, 403, FORBIDDEN_MESSAGE);
                }
            }
        }
    }
}
