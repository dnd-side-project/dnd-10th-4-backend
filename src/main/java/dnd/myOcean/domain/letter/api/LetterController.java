package dnd.myOcean.domain.letter.api;

import dnd.myOcean.domain.letter.application.LetterService;
import dnd.myOcean.domain.letter.dto.request.LetterDeleteRequest;
import dnd.myOcean.domain.letter.dto.request.LetterReadRequest;
import dnd.myOcean.domain.letter.dto.request.LetterReplyRequest;
import dnd.myOcean.domain.letter.dto.request.LetterSendRequest;
import dnd.myOcean.domain.letter.dto.request.LetterStoreRequest;
import dnd.myOcean.domain.letter.dto.response.LetterResponse;
import dnd.myOcean.global.auth.aop.AssignCurrentMemberId;
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

    /**
     * 편지 전송
     */
    @PostMapping("/send")
    @AssignCurrentMemberId
    public ResponseEntity<Void> send(@RequestBody LetterSendRequest request) {
        letterService.send(request);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 1. 보낸 편지
    // 1-1. 단건 조회 O
    // 1-2. 삭제 (실제 삭제 X, 프로퍼티 값 변경) O
    // TODO : 1-3. 전체 페이징 조회(삭제하지 않은 메시지만 페이징)

    // 1-1. 단건 조회 O
    @GetMapping("/send/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<LetterResponse> readSentLetter(@RequestBody LetterReadRequest request,
                                                         @PathVariable("letterId") Long letterId) {
        return new ResponseEntity(letterService.readSendLetter(request, letterId), HttpStatus.OK);
    }

    // 1-2. 삭제 (실제 삭제 X, 프로퍼티 값 변경) O
    @PatchMapping("/send/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> deleteSentLetter(@RequestBody LetterDeleteRequest request,
                                                 @PathVariable("letterId") Long letterId) {
        letterService.deleteSendLetter(request, letterId);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 2. 받은 편지
    // 2-1. 단건 조회(프로퍼티 값 변경) O
    // 2-2. 전체 조회
    // 2-3. 받은 편지 보관 (프로퍼티 값 변경) O
    // 2-4. 받은 편지에 대한 답장 설정 O -> 보낸 사람에게 이메일 알림
    // 2-5. 받은 편지 다른 사람에게 토스 -> 받은 사람들에게 이메일 알림

    // 2-1. 단건 조회(프로퍼티 값 변경) O
    @GetMapping("/reception/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<LetterResponse> readReceivedLetter(@RequestBody LetterReadRequest request,
                                                             @PathVariable("letterId") Long letterId) {
        return new ResponseEntity<>(letterService.readReceivedLetter(request, letterId), HttpStatus.OK);
    }

    // 2-3. 받은 편지 보관 (프로퍼티 값 변경) O
    @PatchMapping("/reception/storage/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> storeReceivedLetter(@RequestBody LetterStoreRequest request,
                                                    @PathVariable("letterId") Long letterId) {
        letterService.storeReceivedLetter(request, letterId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 2-4. 받은 편지에 대한 답장 설정 -> 보낸 사람에게 이메일 알림
    @PatchMapping("/reception/reply/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> replyReceivedLetter(@RequestBody LetterReplyRequest request,
                                                    @PathVariable("letterId") Long letterId) {
        letterService.replyReceivedLetter(request, letterId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 보관한 편지
    // 1. 단건 조회
    // 2. 전체 페이징 조회
    // 3. 보관한 편지 삭제

    // 답장 받은 편지
    // 1. 전체 조회
    // 2. 단건 조회
}
