package dnd.myOcean.global.auth.aop;


import dnd.myOcean.global.auth.exception.auth.AccessDeniedException;
import dnd.myOcean.global.auth.exception.auth.AuthenticationEntryPointException;
import dnd.myOcean.global.auth.exception.auth.IllegalAuthenticationException;
import dnd.myOcean.global.auth.oAuth.kakao.details.KakaoMemberDetails;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AssignCurrentEmailAspect {

    @Before("@annotation(dnd.myOcean.global.auth.aop.AssignCurrentEmail)")
    public void assignMemberId(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs())
                .forEach(arg -> getMethod(arg.getClass(), "setEmail")
                        .ifPresent(
                                setMemberId -> invokeMethod(arg, setMemberId, getCurrentEmail())));
    }

    private void invokeMethod(Object arg, Method method, String currentEmail) {
        try {
            method.invoke(arg, currentEmail);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Method> getMethod(Class<?> clazz, String methodName) {
        try {
            return Optional.of(clazz.getMethod(methodName, String.class));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    private String getCurrentEmail() {
        return getCurrentEmailCheck()
                .orElseThrow(AccessDeniedException::new);
    }

    private Optional<String> getCurrentEmailCheck() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

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
}
