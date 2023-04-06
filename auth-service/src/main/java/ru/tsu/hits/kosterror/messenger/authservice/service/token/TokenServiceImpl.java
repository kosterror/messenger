package ru.tsu.hits.kosterror.messenger.authservice.service.token;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Сервис для работы с токенами, реализующий интерфейс {@link TokenService}.
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtEncoder encoder;
    @Value("${token.access.lifetime-min}")
    private Integer lifetime;

    @Override
    public String generateToken(String login) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(lifetime, ChronoUnit.MINUTES);

        JwtClaimsSet claims = JwtClaimsSet
                .builder()
                .issuer("issuer")
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .subject(login)
                .build();

        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
