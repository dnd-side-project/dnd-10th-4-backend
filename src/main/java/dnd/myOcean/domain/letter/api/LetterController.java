package dnd.myOcean.domain.letter.api;

import dnd.myOcean.domain.letter.application.LetterService;
import dnd.myOcean.domain.letter.dto.request.LetterSendRequest;
import dnd.myOcean.global.auth.aop.AssignCurrentEmail;
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
    
    @AssignCurrentEmail
    @PostMapping
    public ResponseEntity sendLetter(@RequestBody LetterSendRequest request) {
        letterService.sendLetter(request);
        return new ResponseEntity(HttpStatus.OK);
    }
}
