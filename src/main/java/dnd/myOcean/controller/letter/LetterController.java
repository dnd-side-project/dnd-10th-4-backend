package dnd.myOcean.controller.letter;

import dnd.myOcean.aop.AssignCurrentEmail;
import dnd.myOcean.dto.letter.request.LetterSendRequest;
import dnd.myOcean.service.letter.LetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * TODO : 편지 전송 기능
     */
    @AssignCurrentEmail
    @PostMapping
    public ResponseEntity sendLetter(@RequestBody LetterSendRequest request) {
        letterService.sendLetter(request);
        return new ResponseEntity(HttpStatus.OK);
    }
}
