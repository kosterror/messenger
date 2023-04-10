package ru.tsu.hits.kosterror.messenger.coresecurity.security.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.tsu.hits.kosterror.messenger.core.exception.UnauthorizedException;
import ru.tsu.hits.kosterror.messenger.coresecurity.model.JwtPersonData;
import ru.tsu.hits.kosterror.messenger.coresecurity.security.authenticationtoken.JwtAuthentication;
import ru.tsu.hits.kosterror.messenger.coresecurity.service.errorsender.HttpErrorSender;
import ru.tsu.hits.kosterror.messenger.coresecurity.service.jwt.decoder.JwtDecoderService;
import ru.tsu.hits.kosterror.messenger.coresecurity.util.Constants;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Фильтр для валидации JWT токена и извлечения из него полезной информации.
 */
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private static final String UNAUTHORIZED_MESSAGE = "Не авторизован";
    private final JwtDecoderService jwtDecoder;
    private final HttpErrorSender errorSender;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = request.getHeader(Constants.HEADER_JWT);
        log.info(
                "Верификация токена: '{}'. На запрос: '{}' '{}' ",
                token,
                request.getMethod(),
                request.getRequestURI()
        );

        if (token != null && Pattern.matches(Constants.JWT_PATTERN, token)) {
            try {
                JwtPersonData personData = jwtDecoder.getPersonData(token.substring(7));
                Authentication authentication = new JwtAuthentication(personData);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (UnauthorizedException exception) {
                errorSender.sendError(response, 401, UNAUTHORIZED_MESSAGE);
                return;
            }
        } else {
            errorSender.sendError(response, 401, UNAUTHORIZED_MESSAGE);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
