package dnd.myOcean.global.auth.exception.handler;

import dnd.myOcean.global.auth.exception.auth.AccessDeniedException;
import dnd.myOcean.global.auth.exception.auth.AccessTokenExpiredException;
import dnd.myOcean.global.auth.exception.auth.AuthenticationEntryPointException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exception")
public class AuthExceptionController {

    @GetMapping("/access-denied")
    public void accessDeniedException() {
        throw new AccessDeniedException();
    }

    @GetMapping("/entry-point")
    public void authenticateException() {
        throw new AuthenticationEntryPointException();
    }

    @GetMapping("/access-token-expired")
    public void accessTokenExpiredException() {
        throw new AccessTokenExpiredException();
    }
}
