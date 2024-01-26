package dnd.myOcean.config.security.util;

import dnd.myOcean.config.oAuth.kakao.details.KakaoMemberDetails;
import dnd.myOcean.exception.auth.AccessDeniedException;
import dnd.myOcean.exception.auth.AuthenticationEntryPointException;
import dnd.myOcean.exception.auth.IllegalAuthenticationException;
import java.util.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Optional<String> getCurrentEmailCheck() {
        final Authentication authentication = getAuthenticationFromContext();
        Object principal = authentication.getPrincipal();

        if (authentication == null) {
            return Optional.empty();
        }

        if (principal instanceof KakaoMemberDetails) {
            KakaoMemberDetails kakaoMemberDetails = (KakaoMemberDetails) authentication.getPrincipal();
            return Optional.ofNullable(kakaoMemberDetails.getName());
        }
        
        if (principal instanceof String) {
            throw new AuthenticationEntryPointException();
        }

        throw new IllegalAuthenticationException();
    }

    public static String getCurrentEmail() {
        return getCurrentEmailCheck()
                .orElseThrow(AccessDeniedException::new);
    }

    public static boolean isAuthenticated() {
        Authentication authentication = getAuthenticationFromContext();
        return authentication.isAuthenticated() && authentication instanceof UsernamePasswordAuthenticationToken;
    }

    private static Authentication getAuthenticationFromContext() {
        return SecurityContextHolder.getContext()
                .getAuthentication();
    }
}
