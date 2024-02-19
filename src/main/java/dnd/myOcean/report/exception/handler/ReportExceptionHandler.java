package dnd.myOcean.report.exception.handler;

import dnd.myOcean.report.exception.AlreadyReportExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ReportExceptionHandler {

    private final HttpHeaders httpHeaders;

    @ExceptionHandler(AlreadyReportExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity alreadyReportExistException(AlreadyReportExistException e) {
        return new ResponseEntity("이미 신고한 편지입니다.", httpHeaders, HttpStatus.BAD_REQUEST);
    }
}
