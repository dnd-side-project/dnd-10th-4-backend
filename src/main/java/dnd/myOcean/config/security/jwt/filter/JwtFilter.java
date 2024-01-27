package dnd.myOcean.config.security.jwt.filter;

import dnd.myOcean.config.security.jwt.token.TokenProvider;
import dnd.myOcean.dto.jwt.response.TokenDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

    private static final String ACCESS_HEADER = "AccessToken";
    private static final String REFRESH_HEADER = "RefreshToken";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (isRequestPassURI(request, response, filterChain)) {
            return;
        }

        String accessToken = getTokenFromHeader(request, ACCESS_HEADER);

        if (validateExpire(accessToken)) {
            SecurityContextHolder.getContext().setAuthentication(tokenProvider.getAuthentication(accessToken));
        }

        if (!validateExpire(accessToken)) {
            String refreshToken = getTokenFromHeader(request, REFRESH_HEADER);

            // accessToken, refreshToken 재발급
            TokenDto tokenDto = tokenProvider.reIssueAccessToken(refreshToken);
            SecurityContextHolder.getContext()
                    .setAuthentication(tokenProvider.getAuthentication(tokenDto.getAccessToken()));

            redirectReissueURI(request, response, tokenDto);

            return;
        }

        filterChain.doFilter(request, response);
    }

    private static boolean isRequestPassURI(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain)
            throws IOException, ServletException {
        if (request.getRequestURI().startsWith("/api/sign")) {
            filterChain.doFilter(request, response);
            return true;
        }

        if (request.getRequestURI().startsWith("/api/exception")) {
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
        return StringUtils.hasText(token) && tokenProvider.validateExpire(token);
    }

    private static void redirectReissueURI(HttpServletRequest request, HttpServletResponse response, TokenDto tokenDto)
            throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("accessToken", tokenDto.getAccessToken());
        session.setAttribute("refreshToken", tokenDto.getRefreshToken());
        response.sendRedirect("/api/sign/reissue");
    }
}
