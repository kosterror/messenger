package ru.tsu.hits.kosterror.messenger.coresecurity.service.jwt.decoder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.core.exception.UnauthorizedException;
import ru.tsu.hits.kosterror.messenger.coresecurity.config.SecurityProperties;
import ru.tsu.hits.kosterror.messenger.coresecurity.model.JwtPersonData;
import ru.tsu.hits.kosterror.messenger.coresecurity.util.Constants;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.UUID;

/**
 * Сервис для верификации из извлечения информации из payload JWT токена.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtDecoderServiceImpl implements JwtDecoderService {

    private final SecurityProperties properties;

    @Override
    public JwtPersonData getPersonData(String token) {
        try {
            log.info("Верификация токена '{}'", token);
            Key key = Keys.hmacShaKeyFor(properties.getJwtToken().getSecretKey().getBytes(StandardCharsets.UTF_8));
            Jws<Claims> data = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = data.getBody();

            return new JwtPersonData(
                    claims.getSubject(),
                    claims.get(Constants.CLAIM_EMAIL, String.class),
                    claims.get(Constants.CLAIM_FULL_NAME, String.class),
                    UUID.fromString(claims.get(Constants.CLAIM_ID, String.class))
            );
        } catch (Exception exception) {
            log.error("Ошибка во время верификации токена и извлечения claims", exception);
            throw new UnauthorizedException("Не авторизован");
        }
    }

}
