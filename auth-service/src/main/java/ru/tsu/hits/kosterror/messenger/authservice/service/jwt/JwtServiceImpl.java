package ru.tsu.hits.kosterror.messenger.authservice.service.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.authservice.exception.TokenVerificationException;

import java.time.ZonedDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${token.access.secret-key}")
    private String accessSecret;

    @Value("${token.refresh.secret-key}")
    private String refreshSecret;

    @Value("${token.access.lifetime-min}")
    private Integer accessLifeTime;

    @Value("${token.refresh.lifetime-min}")
    private Integer refreshLifeTime;

    @Value("${token.issuer}")
    private String issuer;

    @Override
    public String generateAccessToken(String login) {
        return generateToken(login, accessSecret, accessLifeTime);
    }

    @Override
    public String generateRefreshToken(String login) {
        return generateToken(login, refreshSecret, refreshLifeTime);
    }

    @Override
    public String extractLogin(String token, String secret) throws TokenVerificationException {
        if (token == null || token.isBlank() || token.isEmpty()) {
            throw new TokenVerificationException("Токен пустой");
        }

        JWTVerifier verifier = JWT
                .require(Algorithm.HMAC256(secret))
                .withIssuer(issuer)
                .build();

        try {
            DecodedJWT decodedJWT = verifier.verify(token);

            return decodedJWT
                    .getClaim("login")
                    .asString();
        } catch (Exception exception) {
            throw new TokenVerificationException("Токен не прошел верификацию");
        }

    }

    private String generateToken(String login, String secret, Integer lifeTime) {
        Date issuedAt = new Date();
        Date expiresAt = Date.from(
                ZonedDateTime
                        .now()
                        .plusMinutes(lifeTime)
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

}
