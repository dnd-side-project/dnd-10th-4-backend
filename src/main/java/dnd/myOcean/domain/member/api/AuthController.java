package dnd.myOcean.domain.member.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import dnd.myOcean.domain.member.application.AuthService;
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

    /**
     * 실제 배포에서 사용하는 로그인 API
     */
    @GetMapping("/login/kakao")
    public ResponseEntity<LoginResponse> loginKakao(HttpServletRequest request) {
        String accessToken = (String) request.getSession().getAttribute("accessToken");
        String refreshToken = (String) request.getSession().getAttribute("refreshToken");
        boolean isFirstLogin = (Boolean) request.getSession().getAttribute("isFirstLogin");
        return new ResponseEntity(LoginResponse.of(accessToken, refreshToken, isFirstLogin), HttpStatus.OK);
    }

    /**
     * 포스트맨으로 테스트하기 위한 로그인 API
     */
    @PostMapping("/login/kakao/postman")
    public ResponseEntity<TokenResponse> loginKakao(@RequestParam(name = "code") String code)
            throws JsonProcessingException {
        return new ResponseEntity(authService.kakaoLogin(code), HttpStatus.OK);
    }

    /**
     * 액세스 토큰 재발급 API
     */
    @GetMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueToken(HttpServletRequest request) {
        return new ResponseEntity<>(authService.reissueAccessToken(request), HttpStatus.OK);
    }
}
