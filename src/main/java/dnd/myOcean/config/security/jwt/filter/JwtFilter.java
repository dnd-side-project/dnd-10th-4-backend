package dnd.myOcean.config.security.jwt.filter;

import dnd.myOcean.config.oAuth.kakao.details.KakaoMemberDetails;
import dnd.myOcean.config.security.jwt.token.TokenService;
import dnd.myOcean.dto.jwt.response.TokenDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String ACCESS_HEADER = "AccessToken";
    public static final String REFRESH_HEADER = "RefreshToken";

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/api/sign/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().startsWith("/api/exception")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = getAccessToken(request);
        String refreshToken = getRefreshToken(request);

        if (validateExpire(accessToken) && validateExpire(refreshToken)) {
            log.info("DID1");
            SecurityContextHolder.getContext().setAuthentication(tokenService.getAuthentication(accessToken));
            request.setAttribute(ACCESS_HEADER, accessToken);
            request.setAttribute(ACCESS_HEADER, refreshToken);

        } else if (!validateExpire(accessToken) && validateExpire(refreshToken) && tokenService.validateToken(
                refreshToken)) {
            log.info("DID2");
            Authentication authentication = tokenService.getAuthentication(refreshToken);

            KakaoMemberDetails principal = (KakaoMemberDetails) authentication.getPrincipal();
            String email = principal.getName();

            Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();

            String role = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            TokenDto token = tokenService.createToken(email, role);

            SecurityContextHolder.getContext()
                    .setAuthentication(tokenService.getAuthentication(token.getAccessToken()));

            request.setAttribute(ACCESS_HEADER, token.getAccessToken());

        } else if (validateExpire(accessToken) && !validateExpire(refreshToken) && tokenService.validateToken(
                accessToken)) {
            Authentication authentication = tokenService.getAuthentication(accessToken);

            KakaoMemberDetails principal = (KakaoMemberDetails) authentication.getPrincipal();
            String email = principal.getName();

            Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();

            String role = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));

            TokenDto token = tokenService.createToken(email, role);

            SecurityContextHolder.getContext()
                    .setAuthentication(tokenService.getAuthentication(token.getAccessToken()));

            request.setAttribute(REFRESH_HEADER, token.getRefreshToken());
        }

        filterChain.doFilter(request, response);
    }

    private static String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(ACCESS_HEADER);
        if (StringUtils.hasText(accessToken)) {
            return accessToken;
        }
        return null;
    }

    private String getRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(REFRESH_HEADER);
        if (StringUtils.hasText(refreshToken)) {
            return refreshToken;
        }
        return null;
    }

    private boolean validateExpire(String token) {
        return StringUtils.hasText(token) && tokenService.validateExpire(token);
    }
}
