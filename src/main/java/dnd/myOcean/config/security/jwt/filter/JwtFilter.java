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
    public static final String LOGIN_PATH = "/api/auth/login";
    public static final String EXCEPTION_PATH = "/api/exception";


    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessToken(request);
        String refreshToken = getRefreshToken(request);

        if (validateExpire(accessToken) && validateExpire(refreshToken)) {
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
