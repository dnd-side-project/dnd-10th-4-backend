package dnd.myOcean.member.exception.handler;

import dnd.myOcean.member.exception.AlreadyOnBoardingExecutedException;
import dnd.myOcean.member.exception.BirthdayUpdateLimitExceedException;
import dnd.myOcean.member.exception.ExceedSendLimitException;
import dnd.myOcean.member.exception.GenderUpdateLimitExceedException;
import dnd.myOcean.member.exception.MemberNotFoundException;
import dnd.myOcean.member.exception.NoSuchGenderException;
import dnd.myOcean.member.exception.WorrySelectionRangeLimitException;
import dnd.myOcean.member.exception.WorryTypeContainsNotAccepted;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class MemberExceptionHandler {

    private final HttpHeaders httpHeaders;

    @ExceptionHandler(AlreadyOnBoardingExecutedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity alreadyOnBoardingExecutedException() {
        return new ResponseEntity("이미 온보딩이 완료된 회원입니다.", httpHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BirthdayUpdateLimitExceedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity birthdayUpdateLimitExceedException() {
        return new ResponseEntity("생일 수정 가능한 횟수를 초과했습니다.", httpHeaders, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(GenderUpdateLimitExceedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity genderUpdateLimitExceedException() {
        return new ResponseEntity("성별 수정 가능한 횟수를 초과했습니다.", httpHeaders, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity memberNotFoundException() {
        return new ResponseEntity("존재하지 않는 회원입니다.", httpHeaders, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WorrySelectionRangeLimitException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity worrySelectionRangeLimitException() {
        return new ResponseEntity("고민은 최소 1개, 최대 3개까지만 가능합니다.", httpHeaders, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(WorryTypeContainsNotAccepted.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity worryTypeContainsNotAccepted() {
        return new ResponseEntity("올바르지 않은 고민이 포함되어 있습니다.", httpHeaders, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NoSuchGenderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity noSuchGenderException() {
        return new ResponseEntity("성별은 남자 또는 여자만 선택할 수 있습니다.", httpHeaders, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExceedSendLimitException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity exceedSendLimitException() {
        return new ResponseEntity("하루에 보낼 수 있는 편지를 모두 사용하였습니다.", httpHeaders, HttpStatus.BAD_REQUEST);
    }
}
