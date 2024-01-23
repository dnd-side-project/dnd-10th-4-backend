package dnd.myOcean.advisor;

import dnd.myOcean.exception.auth.AccessDeniedException;
import dnd.myOcean.exception.auth.AuthenticationEntryPointException;
import dnd.myOcean.exception.member.BirthdayUpdateLimitExceedException;
import dnd.myOcean.exception.member.GenderUpdateLimitExceedException;
import dnd.myOcean.exception.member.NoSuchGenderException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvisor {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity accessDeniedException(AccessDeniedException e) {
        return new ResponseEntity("접근 불가능한 권한입니다.", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
    public ResponseEntity authenticationEntryPointException(AuthenticationEntryPointException e) {
        return new ResponseEntity("로그인이 필요한 요청입니다.", HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }

    @ExceptionHandler(BirthdayUpdateLimitExceedException.class)
    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    public ResponseEntity birthdayUpdateLimitExceedException(BirthdayUpdateLimitExceedException e) {
        return new ResponseEntity("생일 수정 가능한 횟수를 초과했습니다.", HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler(GenderUpdateLimitExceedException.class)
    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    public ResponseEntity genderUpdateLimitExceedException(GenderUpdateLimitExceedException e) {
        return new ResponseEntity("성별 수정 가능한 횟수를 초과했습니다.", HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler(NoSuchGenderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity NoSuchGenderException(NoSuchGenderException e) {
        return new ResponseEntity("성별은 남자 또는 여자만 선택할 수 있습니다.", HttpStatus.BAD_REQUEST);
    }
}
