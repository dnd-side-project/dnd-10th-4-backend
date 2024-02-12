package dnd.myOcean.global.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class UnknownExceptionHandler {

    private final HttpHeaders httpHeaders;

    @ExceptionHandler(UnknownException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity accessDeniedException(UnknownException e) {
        return new ResponseEntity("알 수 없는 에러가 발생했습니다. 관리자에게 문의해주세요.",
                httpHeaders,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
