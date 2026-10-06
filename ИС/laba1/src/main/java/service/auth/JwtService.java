package service.auth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dto.AuthData;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import java.security.SecureRandom;
import java.text.ParseException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import service.exception.UnauthorizedException;

@Singleton
@Lock(LockType.READ)
public class JwtService {
    private static final String ISSUER = "flats";
    private byte[] secret;

    @PostConstruct
    public void init() {
        String configured = System.getenv("JWT_SECRET");
        if (configured == null || configured.isBlank()) {
            secret = new byte[32];
            new SecureRandom().nextBytes(secret);
        } else {
            secret = Base64.getDecoder().decode(configured);
            if (secret.length < 32) {
                throw new IllegalStateException("JWT_SECRET must encode at least 32 random bytes in Base64");
            }
        }
    }

    public AuthData issue(String username) {
        Instant now = Instant.now();
        Instant expires = now.plusSeconds(3600);
        JWTClaimsSet claims = new JWTClaimsSet.Builder().issuer(ISSUER).subject(username)
                .issueTime(Date.from(now)).expirationTime(Date.from(expires)).build();
        SignedJWT jwt = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS256)
                .type(JOSEObjectType.JWT).build(), claims);
        try {
            jwt.sign(new MACSigner(secret));
            return new AuthData(jwt.serialize(), username, expires.getEpochSecond());
        } catch (JOSEException exception) {
            throw new IllegalStateException("Cannot issue JWT", exception);
        }
    }

    public Identity verify(String token) {
        try {
            if (token == null || token.length() > 8192) {
                throw new UnauthorizedException("Войдите в систему");
            }
            SignedJWT jwt = SignedJWT.parse(token);
            if (!JWSAlgorithm.HS256.equals(jwt.getHeader().getAlgorithm())
                    || !JOSEObjectType.JWT.equals(jwt.getHeader().getType())
                    || !jwt.verify(new MACVerifier(secret))) {
                throw new UnauthorizedException("Недействительный токен. Войдите снова");
            }
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            Date expires = claims.getExpirationTime();
            Date issued = claims.getIssueTime();
            Instant now = Instant.now();
            if (!ISSUER.equals(claims.getIssuer()) || claims.getSubject() == null
                    || !claims.getSubject().matches("[a-z0-9_]{3,32}") || expires == null || issued == null
                    || !expires.toInstant().isAfter(now) || issued.toInstant().isAfter(now)
                    || !expires.after(issued) || expires.getTime() - issued.getTime() > 3_600_000) {
                throw new UnauthorizedException("Срок действия токена истёк или токен недействителен. Войдите снова");
            }
            return new Identity(claims.getSubject(), expires.toInstant().getEpochSecond());
        } catch (ParseException | JOSEException | IllegalArgumentException exception) {
            throw new UnauthorizedException("Недействительный токен. Войдите снова");
        }
    }

    public record Identity(String username, long expiresAt) {
    }
}
