package dnd.myOcean.letter.api;

import dnd.myOcean.global.auth.aop.AssignCurrentMemberId;
import dnd.myOcean.global.auth.aop.dto.CurrentMemberIdRequest;
import dnd.myOcean.letter.application.LetterService;
import dnd.myOcean.letter.domain.dto.request.LetterReplyRequest;
import dnd.myOcean.letter.domain.dto.request.LetterSendRequest;
import dnd.myOcean.letter.domain.dto.response.ReceivedLetterResponse;
import dnd.myOcean.letter.domain.dto.response.RepliedLetterResponse;
import dnd.myOcean.letter.domain.dto.response.SendLetterResponse;
import dnd.myOcean.letter.repository.infra.querydsl.dto.LetterReadCondition;
import dnd.myOcean.letter.repository.infra.querydsl.dto.PagedSendLettersResponse;
import dnd.myOcean.letter.repository.infra.querydsl.dto.PagedStoredLetterResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/letter")
public class LetterController {

    private final LetterService letterService;

    // 편지 전송
    @PostMapping
    @AssignCurrentMemberId
    public ResponseEntity<Void> send(@ModelAttribute LetterSendRequest request) throws IOException {
        letterService.send(request);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    // 보낸 편지 단건 조회
    @GetMapping("/send/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<SendLetterResponse> readSentLetter(CurrentMemberIdRequest request,
                                                             @PathVariable("letterId") Long letterId) {
        return new ResponseEntity(letterService.readSendLetter(request, letterId), HttpStatus.OK);
    }

    // 보낸 편지 삭제 (실제 삭제 X, 프로퍼티 값 변경)
    @PatchMapping("/send/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> deleteSentLetter(CurrentMemberIdRequest request,
                                                 @PathVariable("letterId") Long letterId) {
        letterService.deleteSendLetter(request, letterId);
        return new ResponseEntity(HttpStatus.OK);
    }

    // 보낸 편지 전체 조회 (페이징)
    @GetMapping("/send")
    @AssignCurrentMemberId
    public ResponseEntity<PagedSendLettersResponse> readSentLetter(@Valid LetterReadCondition cond) {
        return new ResponseEntity(letterService.readSendLetters(cond), HttpStatus.OK);
    }

    // 받은 편지 단건 조회
    @GetMapping("/reception/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<ReceivedLetterResponse> readReceivedLetter(CurrentMemberIdRequest request,
                                                                     @PathVariable("letterId") Long letterId) {
        return new ResponseEntity<>(letterService.readReceivedLetter(request, letterId), HttpStatus.OK);
    }

    // 받은 편지 전체 조회
    @GetMapping("/reception")
    @AssignCurrentMemberId
    public ResponseEntity<List<ReceivedLetterResponse>> readReceivedLetters(CurrentMemberIdRequest request) {
        return new ResponseEntity(letterService.readReceivedLetters(request), HttpStatus.OK);
    }

    // 받은 편지에 대한 답장 설정
    @PatchMapping("/reception/reply/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> replyReceivedLetter(@ModelAttribute LetterReplyRequest request,
                                                    @PathVariable("letterId") Long letterId) throws IOException {
        letterService.replyReceivedLetter(request, letterId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 받은 편지를 답장하지 않고 다른 사람에게 패스
    @PatchMapping("/reception/pass/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> passReceivedLetter(CurrentMemberIdRequest request,
                                                   @PathVariable("letterId") Long letterId) {
        letterService.passReceivedLetter(request, letterId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 답장 받은 편지 전체 조회
    @GetMapping("/reply")
    @AssignCurrentMemberId
    public ResponseEntity<List<RepliedLetterResponse>> readRepliedLetters(CurrentMemberIdRequest request) {
        return new ResponseEntity<>(letterService.readRepliedLetters(request), HttpStatus.OK);
    }

    // 답장 받은 편지 단건 조회
    @GetMapping("/reply/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<RepliedLetterResponse> readRepliedLetter(CurrentMemberIdRequest request,
                                                                   @PathVariable("letterId") Long letterId) {
        return new ResponseEntity<>(letterService.readRepliedLetter(request, letterId), HttpStatus.OK);
    }

    // 답장 받은 편지 보관
    @PatchMapping("/reply/storage/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> storeRepliedLetter(CurrentMemberIdRequest request,
                                                   @PathVariable("letterId") Long letterId) {
        letterService.storeRepliedLetter(request, letterId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 보관한 편지 전체 조회 (페이징)
    @GetMapping("/storage")
    @AssignCurrentMemberId
    public ResponseEntity<PagedStoredLetterResponse> readStoredLetters(@Valid LetterReadCondition cond) {
        return new ResponseEntity<>(letterService.readStoredLetters(cond), HttpStatus.OK);
    }

    // 보관한 편지 보관 해제
    @PatchMapping("/storage/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> deleteStoredLetter(CurrentMemberIdRequest request,
                                                   @PathVariable("letterId") Long letterId) {
        letterService.deleteStoredLetter(request, letterId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
