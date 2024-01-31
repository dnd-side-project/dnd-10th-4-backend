package dnd.myOcean.domain.member.exception.handler;

import dnd.myOcean.domain.member.exception.exceptions.AlreadyExistNicknameException;
import dnd.myOcean.domain.member.exception.exceptions.BirthdayUpdateLimitExceedException;
import dnd.myOcean.domain.member.exception.exceptions.GenderUpdateLimitExceedException;
import dnd.myOcean.domain.member.exception.exceptions.MemberNotFoundException;
import dnd.myOcean.domain.member.exception.exceptions.NoSuchGenderException;
import dnd.myOcean.domain.member.exception.exceptions.SameNicknameModifyRequestException;
import dnd.myOcean.domain.member.exception.exceptions.WorrySelectionRangeLimitException;
import dnd.myOcean.domain.member.exception.exceptions.WorryTypeContainsNotAccepted;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(BirthdayUpdateLimitExceedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity birthdayUpdateLimitExceedException(BirthdayUpdateLimitExceedException e) {
        return new ResponseEntity("생일 수정 가능한 횟수를 초과했습니다.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(GenderUpdateLimitExceedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity genderUpdateLimitExceedException(GenderUpdateLimitExceedException e) {
        return new ResponseEntity("성별 수정 가능한 횟수를 초과했습니다.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(WorrySelectionRangeLimitException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity worrySelectionRangeLimitException(WorrySelectionRangeLimitException e) {
        return new ResponseEntity("고민은 최소 1개, 최대 3개까지만 가능합니다.", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(WorryTypeContainsNotAccepted.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity worryTypeContainsNotAccepted(WorryTypeContainsNotAccepted e) {
        return new ResponseEntity("올바르지 않은 고민이 포함되어 있습니다.", HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NoSuchGenderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity noSuchGenderException(NoSuchGenderException e) {
        return new ResponseEntity("성별은 남자 또는 여자만 선택할 수 있습니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistNicknameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity alreadyExistNicknameException(AlreadyExistNicknameException e) {
        return new ResponseEntity("이미 사용 중인 닉네임 입니다.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SameNicknameModifyRequestException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity sameNicknameModifyRequestException(SameNicknameModifyRequestException e) {
        return new ResponseEntity("현재 로그인한 계정에서 이미 사용 중인 닉네임입니다.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity memberNotFoundException(MemberNotFoundException e) {
        return new ResponseEntity("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND);
    }
}