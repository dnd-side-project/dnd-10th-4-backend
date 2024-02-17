package dnd.myOcean.global.auth.jwt.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFailEntryPoint implements AuthenticationEntryPoint {

    private static final String EXCEPTION_ENTRY_POINT = "/api/exception/entry-point";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        if (!request.isSecure()) {
            String redirectUrl =
                    "https://" + request.getServerName() + EXCEPTION_ENTRY_POINT;
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect(EXCEPTION_ENTRY_POINT);
        }
    }
}
