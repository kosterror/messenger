package ru.tsu.hits.kosterror.messenger.coresecurity.service.jwt.encoder;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.coresecurity.config.SecurityProperties;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import static ru.tsu.hits.kosterror.messenger.coresecurity.util.Constants.*;

/**
 * Сервис для генерации токенов, реализующий интерфейс {@link JwtEncoderService}.
 */
@Service
@RequiredArgsConstructor
public class JwtEncoderServiceImpl implements JwtEncoderService {

    private final SecurityProperties securityProperties;

    @Override
    public String generateToken(String login, UUID id, String email, String fullName) {
        Key key = Keys.hmacShaKeyFor(securityProperties.getJwtToken().getSecretKey().getBytes(StandardCharsets.UTF_8));
        Date issuedAt = Date.from(Instant.now());
        Date expiresAt = Date.from(
                Instant.now()
                        .plus(
                                securityProperties.getJwtToken().getExpirationMinutes(),
                                ChronoUnit.MINUTES
                        )
        );

        return Jwts
                .builder()
                .setIssuer(ISSUER)
                .setSubject(login)
                .claim(CLAIM_ID, id.toString())
                .claim(CLAIM_EMAIL, email)
                .claim(CLAIM_FULL_NAME, fullName)
                .setIssuedAt(issuedAt)
                .setExpiration(expiresAt)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}
