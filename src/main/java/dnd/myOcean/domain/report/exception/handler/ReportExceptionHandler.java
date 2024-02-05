package dnd.myOcean.domain.report.exception.handler;

import dnd.myOcean.domain.report.exception.letterSendBlockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReportExceptionHandler {

    @ExceptionHandler(letterSendBlockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity letterSendBlockException(letterSendBlockException e) {
        return new ResponseEntity("차단된 상대방에게는 편지를 보낼 수 없습니다.", HttpStatus.BAD_REQUEST);
    }
}
