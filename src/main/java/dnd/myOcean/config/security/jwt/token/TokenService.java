package dnd.myOcean.config.security.jwt.token;

import dnd.myOcean.config.oAuth.kakao.details.KakaoMemberDetails;
import dnd.myOcean.domain.refreshtoken.RefreshToken;
import dnd.myOcean.dto.jwt.response.TokenDto;
import dnd.myOcean.repository.redis.RefreshTokenRedisRepository;
import dnd.myOcean.util.HttpRequestUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class TokenService {

    private static final String AUTH_KEY = "AUTHORITY";
    private static final String AUTH_EMAIL = "EMAIL";

    private final String secretKey;
    private final long accessTokenValidityMilliSeconds;
    private final long refreshTokenValidityMilliSeconds;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;
    private Key secretkey;

    public TokenService(@Value("${jwt.secret_key}") String secretKey,
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
        this.secretkey = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto createToken(String email, String role) {
        long now = (new Date()).getTime();

        Date accessValidity = new Date(now + this.accessTokenValidityMilliSeconds);
        Date refreshValidity = new Date(now + this.refreshTokenValidityMilliSeconds);

        String accessToken = Jwts.builder()
                .addClaims(Map.of(AUTH_EMAIL, email))
                .addClaims(Map.of(AUTH_KEY, role))
                .signWith(secretkey, SignatureAlgorithm.HS256)
                .setExpiration(accessValidity)
                .compact();

        String refreshToken = Jwts.builder()
                .addClaims(Map.of(AUTH_EMAIL, email))
                .addClaims(Map.of(AUTH_KEY, role))
                .signWith(secretkey, SignatureAlgorithm.HS256)
                .setExpiration(refreshValidity)
                .compact();

        return TokenDto.of(accessToken, refreshToken);
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

        KakaoMemberDetails principal = new KakaoMemberDetails(
                (String) claims.get(AUTH_EMAIL),
                simpleGrantedAuthorities, Map.of());

        return new UsernamePasswordAuthenticationToken(principal, token, simpleGrantedAuthorities);
    }

    public boolean validateToken(String token) {
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
    public TokenDto reIssueAccessToken(String refreshToken, HttpServletRequest request) {
        if (validateToken(refreshToken) && validateExpire(refreshToken)) {
            RefreshToken findToken = refreshTokenRedisRepository.findByRefreshToken(refreshToken);
            String currentIp = HttpRequestUtil.getClientIp(request);

            if (findToken.isIpEqualTo(currentIp)) {
                TokenDto tokenDto = createToken(findToken.getId(), findToken.getAuthority());
                refreshTokenRedisRepository.save(RefreshToken.builder()
                        .id(findToken.getId())
                        .ip(currentIp)
                        .authorities(findToken.getAuthorities())
                        .refreshToken(tokenDto.getRefreshToken())
                        .build());

                return tokenDto;
            }
        }
        throw new RuntimeException("FAIL");
    }
}
