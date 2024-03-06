package dnd.myOcean.global.auth.exception.handler;

import dnd.myOcean.global.auth.exception.auth.AccessDeniedException;
import dnd.myOcean.global.auth.exception.auth.AccessTokenExpiredException;
import dnd.myOcean.global.auth.exception.auth.AuthenticationEntryPointException;
import dnd.myOcean.global.auth.exception.auth.InvalidAuthCodeException;
import dnd.myOcean.global.auth.exception.auth.ReissueFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@RequiredArgsConstructor
public class AuthExceptionHandler {

    private final HttpHeaders httpHeaders;

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity accessDeniedException() {
        return new ResponseEntity("접근 불가능한 권한입니다.", httpHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity authenticationEntryPointException() {
        return new ResponseEntity("로그인이 필요한 요청 입니다.", httpHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidAuthCodeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity invalidAuthCodeException() {
        return new ResponseEntity("유효하지 않은 인가 코드입니다.", httpHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessTokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity accessTokenExpiredException() {
        return new ResponseEntity("액세스 토큰이 만료되었습니다.", httpHeaders, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ReissueFailException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity reissueFailException() {
        return new ResponseEntity("리프레시 토큰이 올바르지 않습니다. 다시 로그인해주세요.", httpHeaders, HttpStatus.UNAUTHORIZED);
    }
}
