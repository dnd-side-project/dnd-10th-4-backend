package dnd.myOcean.aop;


import dnd.myOcean.config.security.util.SecurityUtils;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AssignCurrentEmailAspect {

    @Before("@annotation(dnd.myOcean.aop.AssignCurrentEmail)")
    public void assignMemberId(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs())
                .forEach(arg -> getMethod(arg.getClass(), "setEmail")
                        .ifPresent(
                                setMemberId -> invokeMethod(arg, setMemberId, SecurityUtils.getCurrentEmail())));
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
}
