package dnd.myOcean.domain.letter.api;

import dnd.myOcean.domain.letter.application.LetterService;
import dnd.myOcean.domain.letter.dto.request.LetterDeleteRequest;
import dnd.myOcean.domain.letter.dto.request.LetterReadRequest;
import dnd.myOcean.domain.letter.dto.request.LetterSendRequest;
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

    // 보낸 편지
    // 1. 단건 조회
    // 2. 삭제 (실제 삭제 X, 프로퍼티 값 변경)
    // 3. 전체 페이징 조회(삭제하지 않은 메시지만 페이징)
    @GetMapping("/send/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<LetterResponse> readSentLetter(@RequestBody LetterReadRequest request,
                                                         @PathVariable("letterId") Long letterId) {
        return new ResponseEntity(letterService.readSendLetter(request, letterId), HttpStatus.OK);
    }

    @PatchMapping("/send/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> deleteSentLetter(@RequestBody LetterDeleteRequest request,
                                                 @PathVariable("letterId") Long letterId) {
        letterService.deleteSendLetter(request, letterId);
        return new ResponseEntity(HttpStatus.OK);
    }


    // 받은 편지
    // 1. 단건 조회(프로퍼티 값 변경)
    // 2. 전체 조회
    // 3. 받은 편지 보관 (프로퍼티 값 변경)
    @GetMapping("/reception/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<LetterResponse> readReceivedLetter(@RequestBody LetterReadRequest request,
                                                             @PathVariable("letterId") Long letterId) {
        return new ResponseEntity<>(letterService.readReceivedLetter(request, letterId), HttpStatus.OK);
    }

    // 받은 편지에 대한 답장 설정 -> 보낸 사람에게 이메일 알림

    // 받은 편지 다른 사람에게 토스 -> 받은 사람들에게 이메일 알림

    // 보관한 편지
    // 1. 단건 조회
    // 2. 전체 페이징 조회
    // 3. 보관한 편지 삭제

    // 답장 받은 편지
    // 1. 전체 조회
    // 2. 단건 조회
}
