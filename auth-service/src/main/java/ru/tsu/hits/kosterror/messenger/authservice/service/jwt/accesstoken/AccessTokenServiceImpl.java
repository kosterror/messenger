package ru.tsu.hits.kosterror.messenger.authservice.service.jwt.accesstoken;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.authservice.exception.TokenVerificationException;
import ru.tsu.hits.kosterror.messenger.authservice.service.jwt.accesstoken.AccessTokenService;

import java.time.ZonedDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AccessTokenServiceImpl implements AccessTokenService {

    @Value("${token.access.secret-key}")
    private String secret;

    @Value("${token.access.lifetime-min}")
    private Integer lifetime;

    @Value("${token.issuer}")
    private String issuer;

    @Override
    public String generateToken(String login) {
        Date issuedAt = new Date();
        Date expiresAt = Date.from(
                ZonedDateTime.now()
                        .plusMinutes(lifetime)
                        .toInstant()
        );

        return JWT
                .create()
                .withClaim("login", login)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withIssuer(issuer)
                .sign(Algorithm.HMAC256(secret));
    }

    @Override
    public String extractLogin(String token) throws TokenVerificationException {
        if (token == null || token.isEmpty() || token.isBlank()) {
            throw new TokenVerificationException("Токен пуст");
        }

        JWTVerifier verifier = JWT
                .require(Algorithm.HMAC256(secret))
                .withIssuer(issuer)
                .build();

        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim("login").asString();
        } catch (Exception exception) {
            throw new JWTVerificationException("Токен не прошел проверку");
        }
    }

}
