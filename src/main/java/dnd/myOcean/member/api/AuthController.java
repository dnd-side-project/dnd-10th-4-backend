package dnd.myOcean.member.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import dnd.myOcean.member.application.AuthService;
import dnd.myOcean.global.auth.jwt.token.LoginResponse;
import dnd.myOcean.global.auth.jwt.token.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // 카카오 로그인
    @PostMapping("/login/kakao")
    public ResponseEntity<LoginResponse> loginKakao(@RequestParam(name = "code") String code)
            throws JsonProcessingException {
        LoginResponse loginResponse = authService.kakaoLogin(code);
        return new ResponseEntity(loginResponse, HttpStatus.OK);
    }

    // 카카오 로그인 API for Postman
    @PostMapping("/login/kakao/postman")
    public ResponseEntity<LoginResponse> loginKakaoForPostman(@RequestParam(name = "code") String code)
            throws JsonProcessingException {
        LoginResponse loginResponse = authService.kakaoLoginForPostman(code);
        return new ResponseEntity(loginResponse, HttpStatus.OK);
    }

    // 액세스 토큰 재발급
    @GetMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueToken(HttpServletRequest request) {
        return new ResponseEntity<>(authService.reissueAccessToken(request), HttpStatus.OK);
    }
}
