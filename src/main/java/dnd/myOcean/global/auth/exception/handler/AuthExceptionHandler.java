package dnd.myOcean.global.auth.exception.handler;

import dnd.myOcean.global.auth.exception.auth.AccessDeniedException;
import dnd.myOcean.global.auth.exception.auth.AuthenticationEntryPointException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity accessDeniedException(AccessDeniedException e) {
        return new ResponseEntity("접근 불가능한 권한입니다.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity authenticationEntryPointException(AuthenticationEntryPointException e) {
        return new ResponseEntity("로그인이 필요한 요청입니다.", HttpStatus.UNAUTHORIZED);
    }
}
