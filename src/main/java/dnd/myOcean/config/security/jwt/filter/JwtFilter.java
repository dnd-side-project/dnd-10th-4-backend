package dnd.myOcean.config.security.jwt.filter;

import dnd.myOcean.config.security.jwt.token.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String ACCESS_HEADER = "Auth";
    public static final String REFRESH_HEADER = "Refresh";

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

        /**
         * TODO : accessToken, refreshToken 만료에 따른 처리
         * 1. accessToken 정상, refreshToken 정상 -> Context에 authentication 담기 -> doFilter
         * 2. accessToken 만료, refreshToken 정상 -> refreshToken으로 accessToken reIssue -> Context에 authentication 담기 -> doFilter
         * 3. accessToken 정상, refreshToken 만료 -> accessToken으로 refreshToken reIssue -> Context에 authentication 담기 -> doFilter
         * 4. accessToken 만료, refreshToken 만료 -> 재로그인 요청
         */
        if (tokenService.validateToken(accessToken) && tokenService.validateToken(refreshToken)) {
            SecurityContextHolder.getContext().setAuthentication(tokenService.getAuthentication(accessToken));
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
        String accessToken = request.getHeader(REFRESH_HEADER);
        if (StringUtils.hasText(accessToken)) {
            return accessToken;
        }
        return null;
    }

    private boolean validateExpire(String token) {
        return StringUtils.hasText(token) && tokenService.validateExpire(token);
    }
}
