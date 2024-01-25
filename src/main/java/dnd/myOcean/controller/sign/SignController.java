package dnd.myOcean.controller.sign;


import com.fasterxml.jackson.core.JsonProcessingException;
import dnd.myOcean.dto.jwt.response.TokenDto;
import dnd.myOcean.service.sign.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sign")
public class SignController {

    private final SignService signService;

    @GetMapping("/login/kakao")
    public ResponseEntity loginKakao(@RequestParam(name = "accessToken") String accessToken,
                                     @RequestParam(name = "refreshToken") String refreshToken) {
        return new ResponseEntity(TokenDto.of(accessToken, refreshToken), HttpStatus.OK);
    }

    /**
     * for 포스트맨 테스트 API
     */
    @PostMapping("/login/kakao/postman")
    public ResponseEntity loginKakao(@RequestParam(name = "code") String code)
            throws JsonProcessingException {
        return new ResponseEntity(signService.kakaoLogin(code), HttpStatus.OK);
    }
}
