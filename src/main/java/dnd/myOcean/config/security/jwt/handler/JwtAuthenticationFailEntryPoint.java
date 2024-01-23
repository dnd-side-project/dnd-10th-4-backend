package dnd.myOcean.config.security.jwt.handler;

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
        response.sendRedirect(EXCEPTION_ENTRY_POINT);
    }
}
