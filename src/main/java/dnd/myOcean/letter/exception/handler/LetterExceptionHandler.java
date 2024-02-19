package dnd.myOcean.letter.exception.handler;


import dnd.myOcean.letter.exception.AccessDeniedLetterException;
import dnd.myOcean.letter.exception.AlreadyReplyExistException;
import dnd.myOcean.letter.exception.RepliedLetterPassException;
import dnd.myOcean.letter.exception.UnAnsweredLetterStoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class LetterExceptionHandler {

    private final HttpHeaders httpHeaders;

    @ExceptionHandler(AccessDeniedLetterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity accessDeniedLetterException(AccessDeniedLetterException e) {
        return new ResponseEntity("해당 편지에 접근할 수 없습니다.", httpHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyReplyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity alreadyReplyExistException(AlreadyReplyExistException e) {
        return new ResponseEntity("이미 답장을 완료한 편지입니다.", httpHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RepliedLetterPassException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity repliedLetterPassException(RepliedLetterPassException e) {
        return new ResponseEntity("답장한 편지는 패스할 수 없습니다.", httpHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAnsweredLetterStoreException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity unAnsweredLetterStoreException(UnAnsweredLetterStoreException e) {
        return new ResponseEntity("답장하지 않은 편지는 보관할 수 없습니다.", httpHeaders, HttpStatus.BAD_REQUEST);
    }
}

