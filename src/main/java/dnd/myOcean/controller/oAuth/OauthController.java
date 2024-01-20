package dnd.myOcean.controller.oAuth;

import dnd.myOcean.dto.oAuth.response.MemberInfo;
import dnd.myOcean.service.oAuth.OauthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class OauthController {
    private final OauthService kakaoService;

    @GetMapping("/kakao-login")
    public ResponseEntity<MemberInfo> kakaoCalllback(HttpServletRequest request) throws Exception {
        MemberInfo memberInfo = kakaoService.getMemberInfo(request.getParameter("code"));

        return ResponseEntity.ok(memberInfo);
    }
}
