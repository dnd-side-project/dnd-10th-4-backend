package dnd.myOcean.global.auth.jwt.token;

import dnd.myOcean.global.auth.jwt.token.repository.redis.RefreshTokenRedisRepository;
import dnd.myOcean.global.auth.oauth.kakao.details.KakaoMemberDetails;
import dnd.myOcean.global.common.auth.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class TokenProvider {

    private static final String AUTH_ID = "ID";
    private static final String AUTH_KEY = "AUTHORITY";
    private static final String AUTH_EMAIL = "EMAIL";

    private final String secretKey;
    private final long accessTokenValidityMilliSeconds;
    private final long refreshTokenValidityMilliSeconds;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private Key secretkey;

    public TokenProvider(@Value("${jwt.secret_key}") String secretKey,
                         @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValiditySeconds,
                         @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValiditySeconds,
                         RefreshTokenRedisRepository refreshTokenRedisRepository) {
        this.secretKey = secretKey;
        this.accessTokenValidityMilliSeconds = accessTokenValiditySeconds * 1000;
        this.refreshTokenValidityMilliSeconds = refreshTokenValiditySeconds * 1000;
        this.refreshTokenRedisRepository = refreshTokenRedisRepository;
    }

    @PostConstruct
    public void initKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretkey = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public TokenResponse createToken(String memberId, String email, String role) {
        long now = (new Date()).getTime();

        Date accessValidity = new Date(now + this.accessTokenValidityMilliSeconds);
        Date refreshValidity = new Date(now + this.refreshTokenValidityMilliSeconds);

        String accessToken = Jwts.builder()
                .addClaims(Map.of(AUTH_ID, memberId))
                .addClaims(Map.of(AUTH_EMAIL, email))
                .addClaims(Map.of(AUTH_KEY, role))
                .signWith(secretkey, SignatureAlgorithm.HS256)
                .setExpiration(accessValidity)
                .compact();

        String refreshToken = Jwts.builder()
                .addClaims(Map.of(AUTH_ID, memberId))
                .addClaims(Map.of(AUTH_EMAIL, email))
                .addClaims(Map.of(AUTH_KEY, role))
                .signWith(secretkey, SignatureAlgorithm.HS256)
                .setExpiration(refreshValidity)
                .compact();

        return TokenResponse.of(accessToken, refreshToken);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<String> authorities = Arrays.asList(claims.get(AUTH_KEY)
                .toString()
                .split(","));

        List<? extends GrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                .map(auth -> new SimpleGrantedAuthority(auth))
                .collect(Collectors.toList());

        KakaoMemberDetails principal = new KakaoMemberDetails(Long.parseLong((String) claims.get(AUTH_ID)),
                (String) claims.get(AUTH_EMAIL),
                simpleGrantedAuthorities, Map.of());

        return new UsernamePasswordAuthenticationToken(principal, token, simpleGrantedAuthorities);
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            return false;
        } catch (UnsupportedJwtException e) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public boolean validateExpire(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    @Transactional
    public TokenResponse reIssueAccessToken(String refreshToken) {
        RefreshToken findToken = refreshTokenRedisRepository.findByRefreshToken(refreshToken);

        TokenResponse tokenResponse = createToken(String.valueOf(findToken.getId()), findToken.getEmail(),
                findToken.getAuthority());
        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(findToken.getId())
                .authorities(findToken.getAuthorities())
                .refreshToken(tokenResponse.getRefreshToken())
                .build());

        return tokenResponse;
    }
}
