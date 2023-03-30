package ru.tsu.hits.kosterror.messenger.authservice.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.tsu.hits.kosterror.messenger.authservice.exception.TokenVerificationException;
import ru.tsu.hits.kosterror.messenger.authservice.service.jwt.JwtService;
import ru.tsu.hits.kosterror.messenger.authservice.service.servlet.ServletResponseService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final ServletResponseService servletResponseService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null) {
            var token = authHeader.substring(7);
            try {
                var login = jwtService.extractLoginFromAccessToken(token);
                var userDetails = userDetailsService.loadUserByUsername(login);

                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (TokenVerificationException exception) {
                servletResponseService.sendError(response, 401, exception.getMessage());
                return;
            }
        } else {
            servletResponseService.sendError(response, 401, "Нет токена");
            return;
        }

        filterChain.doFilter(request, response);
    }

}
