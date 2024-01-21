package dnd.myOcean.controller.oAuth;

import dnd.myOcean.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class SignController {

    private final TokenService tokenService;

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity kakaoCallback(@RequestParam("code") String code) {
        return new ResponseEntity(code, HttpStatus.OK);
    }
}
