package dnd.myOcean.domain.report.exception.handler;

import dnd.myOcean.domain.report.exception.AlreadyReportExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReportExceptionHandler {

    @ExceptionHandler(AlreadyReportExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity alreadyReportExistException(AlreadyReportExistException e) {
        return new ResponseEntity("이미 신고한 편지입니다.", HttpStatus.BAD_REQUEST);
    }
}
