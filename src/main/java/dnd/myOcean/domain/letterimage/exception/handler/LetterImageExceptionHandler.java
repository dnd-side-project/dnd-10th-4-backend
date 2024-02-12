package dnd.myOcean.domain.letterimage.exception.handler;

import dnd.myOcean.domain.letterimage.exception.NoExtException;
import dnd.myOcean.domain.letterimage.exception.UnSupportExtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class LetterImageExceptionHandler {

    private final HttpHeaders httpHeaders;

    @ExceptionHandler(UnSupportExtException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseEntity unSupportExtException(UnSupportExtException e) {
        return new ResponseEntity("업로드 하신 이미지는 지원하지 않는 파일 형식입니다. 이미지가 png, jpg, jpeg, gif, bmp의 형식인지 확인해주세요.",
                httpHeaders,
                HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(NoExtException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseEntity noExtException(NoExtException e) {
        return new ResponseEntity("업로드 하신 이미지에 확장자가 존재하지 않습니다. ",
                httpHeaders,
                HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}
