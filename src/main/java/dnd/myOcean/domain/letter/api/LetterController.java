package dnd.myOcean.domain.letter.api;

import dnd.myOcean.domain.letter.application.LetterService;
import dnd.myOcean.domain.letter.dto.request.LetterReplyRequest;
import dnd.myOcean.domain.letter.dto.request.LetterSendRequest;
import dnd.myOcean.domain.letter.dto.response.ReceivedLetterResponse;
import dnd.myOcean.domain.letter.dto.response.RepliedLetterResponse;
import dnd.myOcean.domain.letter.dto.response.SendLetterResponse;
import dnd.myOcean.domain.letter.repository.infra.querydsl.dto.LetterReadCondition;
import dnd.myOcean.domain.letter.repository.infra.querydsl.dto.PagedReceivedLettersResponse;
import dnd.myOcean.domain.letter.repository.infra.querydsl.dto.PagedRepliedLettersResponse;
import dnd.myOcean.domain.letter.repository.infra.querydsl.dto.PagedSendLettersResponse;
import dnd.myOcean.global.auth.aop.AssignCurrentMemberId;
import dnd.myOcean.global.auth.aop.dto.CurrentMemberIdRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/letter")
public class LetterController {

    private final LetterService letterService;

    // TODO : 0, 2-3, 2-5에 대한 이메일 알림 기능 추가
    // TODO : 생성된 지 48시간이 지났고 && 답장이 존재하지 않는 편지 스케쥴러로 제거

    // 0. 편지 전송
    @PostMapping
    @AssignCurrentMemberId
    public ResponseEntity<Void> send(@RequestBody LetterSendRequest request) {
        letterService.send(request);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 1-1. 보낸 편지 단건 조회
    @GetMapping("/send/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<SendLetterResponse> readSentLetter(@RequestBody CurrentMemberIdRequest request,
                                                             @PathVariable("letterId") Long letterId) {
        return new ResponseEntity(letterService.readSendLetter(request, letterId), HttpStatus.OK);
    }

    // 1-2. 보낸 편지 삭제 (실제 삭제 X, 프로퍼티 값 변경)
    @PatchMapping("/send/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> deleteSentLetter(@RequestBody CurrentMemberIdRequest request,
                                                 @PathVariable("letterId") Long letterId) {
        letterService.deleteSendLetter(request, letterId);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 1-3. 보낸 편지 중 삭제하지 않은 편지 전체 조회 (페이징)
    @GetMapping("/send")
    @AssignCurrentMemberId
    public ResponseEntity<PagedSendLettersResponse> readSentLetter(@RequestBody LetterReadCondition cond) {
        return new ResponseEntity(letterService.readSendLetters(cond), HttpStatus.OK);
    }

    // 2-1. 받은 편지 단건 조회 (프로퍼티 값 변경)
    @GetMapping("/reception/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<ReceivedLetterResponse> readReceivedLetter(@RequestBody CurrentMemberIdRequest request,
                                                                     @PathVariable("letterId") Long letterId) {
        return new ResponseEntity<>(letterService.readReceivedLetter(request, letterId), HttpStatus.OK);
    }

    // 2-2. 받은 편지 전체 조회
    @GetMapping("/reception")
    @AssignCurrentMemberId
    public ResponseEntity<List<ReceivedLetterResponse>> readReceivedLetters(
            @RequestBody CurrentMemberIdRequest request) {
        return new ResponseEntity(letterService.readReceivedLetters(request), HttpStatus.OK);
    }

    // 2-3. 받은 편지에 대한 답장 설정
    @PatchMapping("/reception/reply/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> replyReceivedLetter(@RequestBody LetterReplyRequest request,
                                                    @PathVariable("letterId") Long letterId) {
        letterService.replyReceivedLetter(request, letterId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 2-4. 받은 편지 보관 (프로퍼티 값 변경)
    @PatchMapping("/reception/storage/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> storeReceivedLetter(@RequestBody CurrentMemberIdRequest request,
                                                    @PathVariable("letterId") Long letterId) {
        letterService.storeReceivedLetter(request, letterId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 2-5. 받은 편지를 답장하지 않고 다른 사람에게 패스
    @PatchMapping("/reception/pass/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> passReceivedLetter(@RequestBody CurrentMemberIdRequest request,
                                                   @PathVariable("letterId") Long letterId) {
        letterService.passReceivedLetter(request, letterId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 3-1. 보관한 편지 전체 페이징 조회
    @GetMapping("/storage")
    @AssignCurrentMemberId
    public ResponseEntity<PagedReceivedLettersResponse> readStoredLetters(@RequestBody LetterReadCondition cond) {
        return new ResponseEntity<>(letterService.readStoredLetters(cond), HttpStatus.OK);
    }

    // 3-2. 보관한 편지 보관 해제
    @PatchMapping("/storage/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> deleteStoredLetter(@RequestBody CurrentMemberIdRequest request,
                                                   @PathVariable("letterId") Long letterId) {
        letterService.deleteStoredLetter(request, letterId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 4-1. 답장 받은 편지 전체 조회
    @GetMapping("/reply")
    @AssignCurrentMemberId
    public ResponseEntity<PagedRepliedLettersResponse> readRepliedLetters(@RequestBody LetterReadCondition cond) {
        return new ResponseEntity<>(letterService.readRepliedLetters(cond), HttpStatus.OK);
    }

    // 4-2. 답장 받은 편지 전체 조회
    @GetMapping("/reply/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<RepliedLetterResponse> readRepliedLetter(@RequestBody CurrentMemberIdRequest request,
                                                                   @PathVariable("letterId") Long letterId) {
        return new ResponseEntity<>(letterService.readRepliedLetter(request, letterId), HttpStatus.OK);
    }
}
