package dnd.myOcean.domain.letter.exception.handler;


import dnd.myOcean.domain.letter.exception.AccessDeniedLetterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LetterExceptionHandler {

    @ExceptionHandler(AccessDeniedLetterException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity accessDeniedLetterException(AccessDeniedLetterException e) {
        return new ResponseEntity("해당 편지에 접근할 수 있는 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
}

