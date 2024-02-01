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
    @AssignCurrentMemberId
    @PostMapping("/send")
    public ResponseEntity send(@RequestBody LetterSendRequest request) {
        letterService.send(request);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * 보낸 편지 단건 조회
     */
    @AssignCurrentMemberId
    @GetMapping("/send/{letterId}")
    public ResponseEntity<LetterResponse> readSentLetter(@RequestBody LetterReadRequest request,
                                                         @PathVariable("letterId") Long letterId) {
        return new ResponseEntity(letterService.readSendLetter(request, letterId), HttpStatus.OK);
    }

//
//    /**
//     * 보낸 편지 페이징 조회
//     */
//    @GetMapping("/send")
//

    /**
     * 보낸 편지 삭제
     */
    @AssignCurrentMemberId
    @PatchMapping("/send/{letterId}")
    public ResponseEntity<Void> deleteSentLetter(@RequestBody LetterDeleteRequest request,
                                                 @PathVariable("letterId") Long letterId) {
        letterService.deleteSendLetter(request, letterId);
        return new ResponseEntity(HttpStatus.OK);
    }


    /**
     * 받은 편지 단건 조회
     */
    @AssignCurrentMemberId
    @GetMapping("/reception/{letterId}")
    public ResponseEntity<LetterResponse> readReceivedLetter(@RequestBody LetterReadRequest request,
                                                             @PathVariable("letterId") Long letterId) {
        return new ResponseEntity<>(letterService.readReceivedLetter(request, letterId), HttpStatus.OK);
    }

//
//    /**
//     * 받은 편지 다른 사람에게 토스
//     */
//    @PatchMapping("/toss/{letterId}")
//
//
//    /**
//     * 편지에 대한 답장 단건 조회
//     */
//    @GetMapping("/reply/{letterId}")
}
