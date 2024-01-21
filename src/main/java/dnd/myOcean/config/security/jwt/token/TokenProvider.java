package dnd.myOcean.config.security.jwt.token;

import dnd.myOcean.config.oAuth.kakao.KakaoMemberDetails;
import dnd.myOcean.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
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


@Component
public class TokenProvider {

    private static final String AUTH_KEY = "AUTHORITY";
    private static final String AUTH_EMAIL = "EMAIL";

    private final String secretKey;
    private final long accessTokenValidityMilliSeconds;
    private Key secretkey;

    public TokenProvider(@Value("${jwt.secret_key}") String secretKey,
                         @Value("${jwt.token-validity-in-seconds}") long accessTokenValidityMilliSeconds) {
        this.secretKey = secretKey;
        this.accessTokenValidityMilliSeconds = accessTokenValidityMilliSeconds * 1000;
    }

    @PostConstruct
    public void initKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretkey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication) {
        KakaoMemberDetails principal = (KakaoMemberDetails) authentication.getPrincipal();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.accessTokenValidityMilliSeconds);

        return Jwts.builder()
                .addClaims(Map.of(AUTH_EMAIL, principal.getName()))
                .addClaims(Map.of(AUTH_KEY, authorities))
                .signWith(secretkey, SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .compact();
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
            validate(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new InvalidTokenException();
        } catch (UnsupportedJwtException e) {
            throw new InvalidTokenException();
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
    }

    private void validate(String token) {
        Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }

    public boolean validateExpire(String token) {
        try {
            validate(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        }
    }
}
