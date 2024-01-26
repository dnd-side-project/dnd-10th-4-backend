package dnd.myOcean.config.security.jwt.filter;

import dnd.myOcean.config.security.jwt.token.TokenService;
import dnd.myOcean.dto.jwt.response.TokenDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        if (isRequestPassURI(request, response, filterChain)) {
            return;
        }

        String accessToken = getTokenFromHeader(request, ACCESS_HEADER);

        if (validateExpire(accessToken)) {
            SecurityContextHolder.getContext().setAuthentication(tokenService.getAuthentication(accessToken));
        }

        if (!validateExpire(accessToken)) {
            String refreshToken = getTokenFromHeader(request, REFRESH_HEADER);

            // accessToken, refreshToken 재발급
            TokenDto tokenDto = tokenService.reIssueAccessToken(refreshToken, request);

            SecurityContextHolder.getContext()
                    .setAuthentication(tokenService.getAuthentication(tokenDto.getAccessToken()));
        }

        filterChain.doFilter(request, response);
    }

    private static boolean isRequestPassURI(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain)
            throws IOException, ServletException {
        if (request.getRequestURI().startsWith("/api/sign/login")) {
            filterChain.doFilter(request, response);
            return true;
        }

        if (request.getRequestURI().startsWith("/api/exception")) {
            filterChain.doFilter(request, response);
            return true;
        }

        if (request.getRequestURI().startsWith("/api/token/reIssue")) {
            filterChain.doFilter(request, response);
            return true;
        }

        if (request.getRequestURI().startsWith("/favicon.ico")) {
            filterChain.doFilter(request, response);
            return true;
        }
        
        return false;
    }

    private static String getTokenFromHeader(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        if (StringUtils.hasText(token)) {
            return token;
        }
        return null;
    }

    private boolean validateExpire(String token) {
        return StringUtils.hasText(token) && tokenService.validateExpire(token);
    }
}
