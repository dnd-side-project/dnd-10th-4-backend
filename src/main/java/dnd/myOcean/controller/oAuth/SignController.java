package dnd.myOcean.controller.oAuth;

import dnd.myOcean.dto.oAuth.response.MemberInfo;
import dnd.myOcean.service.oAuth.KakaoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class SignController {

    private final KakaoService kakaoService;

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity<MemberInfo> kakaoCalllback(HttpServletRequest request) throws Exception {
        MemberInfo memberInfo = kakaoService.getMemberInfo(request.getParameter("code"));

        return ResponseEntity.ok(memberInfo);
    }

    @GetMapping("/")
    public String login() {
        return "메인 화면";
    }
}
