package com.rightpair.infra.jwt.service;

import com.rightpair.infra.jwt.dto.JwtPair;
import com.rightpair.infra.jwt.dto.JwtPayload;
import com.rightpair.infra.jwt.entity.JwtBlackListRedisEntity;
import com.rightpair.infra.jwt.entity.JwtRefreshTokenRedisEntity;
import com.rightpair.infra.jwt.exception.JwtExpiredRefreshTokenException;
import com.rightpair.infra.jwt.exception.JwtInvalidRefreshTokenException;
import com.rightpair.infra.jwt.exception.auth.JwtExpiredAuthorizationException;
import com.rightpair.infra.jwt.exception.auth.JwtInvalidSignatureException;
import com.rightpair.infra.jwt.exception.auth.JwtMalformedStructureException;
import com.rightpair.infra.jwt.exception.auth.JwtUnsupportedFormatException;
import com.rightpair.infra.jwt.repository.JwtBlackListRedisRepository;
import com.rightpair.infra.jwt.repository.JwtRefreshTokenRedisRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {
    private static final String CLIENT_ID_KEY = "client_id";
    private static final String CLIENT_EMAIL_KEY = "client_email";

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    @Value("${service.jwt.refresh-expiration}")
    private Long refreshExpiration;

    private final JwtRefreshTokenRedisRepository jwtRefreshTokenRedisRepository;
    private final JwtBlackListRedisRepository jwtBlackListRedisRepository;
    private final SecretKey secretKey;

    public JwtService(@Value("${service.jwt.secret-key}") String secretKey,
                      JwtRefreshTokenRedisRepository jwtRefreshTokenRedisRepository,
                      JwtBlackListRedisRepository jwtBlackListRedisRepository) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.jwtRefreshTokenRedisRepository = jwtRefreshTokenRedisRepository;
        this.jwtBlackListRedisRepository = jwtBlackListRedisRepository;
    }

    public JwtPair createTokenPair(JwtPayload jwtPayload) {
        String accessToken = createToken(jwtPayload, accessExpiration);
        String refreshToken = createToken(jwtPayload, refreshExpiration);

        jwtRefreshTokenRedisRepository.save(
                JwtRefreshTokenRedisEntity.from(jwtPayload.email(), refreshToken, refreshExpiration));

        return JwtPair.from(accessToken, refreshToken, accessExpiration);
    }

    public JwtPair refreshAccessToken(String refreshToken) {
        JwtPayload jwtPayload = verifyToken(refreshToken);

        Optional<String> savedRefreshToken = jwtRefreshTokenRedisRepository.findValueByKey(jwtPayload.email());
        if (savedRefreshToken.isEmpty()) {
            throw new JwtExpiredRefreshTokenException();
        }
        if (!refreshToken.equalsIgnoreCase(savedRefreshToken.get())) {
            throw new JwtInvalidRefreshTokenException();
        }

        JwtPayload newJwtPayload = JwtPayload.fromNow(jwtPayload.id(), jwtPayload.email());

        // RTR
        deleteRefreshToken(jwtPayload.email());

        return createTokenPair(newJwtPayload);
    }

    public void deleteRefreshToken(String email) {
        jwtRefreshTokenRedisRepository.deleteByKey(email);
    }

    public void addAccessTokenToBlackList(String email, String accessToken) {
        // 남은 리프레시 토큰 시간만큼을 블랙리스트 시간으로 처리함
        long refreshTokenExpiredTime = jwtRefreshTokenRedisRepository.getExpire(email);
        jwtBlackListRedisRepository.save(JwtBlackListRedisEntity.from(accessToken, refreshTokenExpiredTime));
    }

    public boolean isAccessTokenBlackListed(String accessToken) {
        return jwtBlackListRedisRepository.findValueByKey(accessToken).isPresent();
    }

    private String createToken(JwtPayload jwtPayload, long expiration) {
        return Jwts.builder()
                .claim(CLIENT_ID_KEY, jwtPayload.id())
                .claim(CLIENT_EMAIL_KEY, jwtPayload.email())
                .issuer(issuer)
                .issuedAt(jwtPayload.issuedAt())
                .expiration(new Date(jwtPayload.issuedAt().getTime() + expiration))
                .signWith(secretKey)
                .compact();
    }

    public JwtPayload verifyToken(String jwtToken) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();
            return JwtPayload.from(
                    claims.get(CLIENT_ID_KEY, String.class),
                    claims.get(CLIENT_EMAIL_KEY, String.class),
                    claims.getIssuedAt());
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredAuthorizationException();
        } catch (MalformedJwtException e) {
            throw new JwtMalformedStructureException();
        } catch (SignatureException e) {
            throw new JwtInvalidSignatureException();
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            throw new JwtUnsupportedFormatException();
        }
    }
}
