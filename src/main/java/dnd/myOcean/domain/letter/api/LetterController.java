package dnd.myOcean.domain.letter.api;

import dnd.myOcean.domain.letter.application.LetterService;
import dnd.myOcean.domain.letter.dto.request.LetterDeleteRequest;
import dnd.myOcean.domain.letter.dto.request.LetterReadRequest;
import dnd.myOcean.domain.letter.dto.request.LetterReplyRequest;
import dnd.myOcean.domain.letter.dto.request.LetterSendRequest;
import dnd.myOcean.domain.letter.dto.request.LetterStoreRequest;
import dnd.myOcean.domain.letter.dto.request.LettersReadRequest;
import dnd.myOcean.domain.letter.dto.response.LetterResponse;
import dnd.myOcean.domain.letter.repository.infra.querydsl.dto.LetterReadCondition;
import dnd.myOcean.domain.letter.repository.infra.querydsl.dto.PagedLettersResponse;
import dnd.myOcean.global.auth.aop.AssignCurrentMemberId;
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

    // TODO : 0, 2-4, 2-5에 대한 이메일 알림 기능 추가

    // 0. 편지 전송
    @PostMapping
    @AssignCurrentMemberId
    public ResponseEntity<Void> send(@RequestBody LetterSendRequest request) {
        letterService.send(request);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 1. 보낸 편지
    // 1-1. 단건 조회
    @GetMapping("/send/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<LetterResponse> readSentLetter(@RequestBody LetterReadRequest request,
                                                         @PathVariable("letterId") Long letterId) {
        return new ResponseEntity(letterService.readSendLetter(request, letterId), HttpStatus.OK);
    }

    // 1-2. 삭제 (실제 삭제 X, 프로퍼티 값 변경)
    @PatchMapping("/send/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> deleteSentLetter(@RequestBody LetterDeleteRequest request,
                                                 @PathVariable("letterId") Long letterId) {
        letterService.deleteSendLetter(request, letterId);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 1-3. 삭제하지 않은 편지 전체 조회 (페이징)
    @GetMapping("/send")
    @AssignCurrentMemberId
    public ResponseEntity<PagedLettersResponse> readSentLetter(@RequestBody LetterReadCondition cond) {
        return new ResponseEntity(letterService.readSendLetters(cond), HttpStatus.OK);
    }

    // 2. 받은 편지
    // 2-1. 받은 편지 단건 조회(프로퍼티 값 변경)
    @GetMapping("/reception/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<LetterResponse> readReceivedLetter(@RequestBody LetterReadRequest request,
                                                             @PathVariable("letterId") Long letterId) {
        return new ResponseEntity<>(letterService.readReceivedLetter(request, letterId), HttpStatus.OK);
    }

    // 2-2. 받은 편지 전체 조회
    @GetMapping("/reception")
    @AssignCurrentMemberId
    public ResponseEntity<List<LetterResponse>> readReceivedLetters(@RequestBody LettersReadRequest request) {
        return new ResponseEntity(letterService.readReceivedLetters(request), HttpStatus.OK);
    }

    // 2-3. 받은 편지 보관 (프로퍼티 값 변경)
    @PatchMapping("/reception/storage/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> storeReceivedLetter(@RequestBody LetterStoreRequest request,
                                                    @PathVariable("letterId") Long letterId) {
        letterService.storeReceivedLetter(request, letterId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 2-4. 받은 편지에 대한 답장 설정
    @PatchMapping("/reception/reply/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> replyReceivedLetter(@RequestBody LetterReplyRequest request,
                                                    @PathVariable("letterId") Long letterId) {
        letterService.replyReceivedLetter(request, letterId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO 2-5. 받은 편지 다른 사람에게 토스

    // 3. 보관한 편지
    // TODO 3-1. 단건 조회
    // TODO 3-2. 전체 페이징 조회
    // TODO 3-3. 보관한 편지 삭제

    // 4. 답장 받은 편지
    // TODO 4-1. 전체 조회
    // TODO 4-2. 단건 조회
}
