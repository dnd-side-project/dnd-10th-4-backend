package dnd.myOcean.controller.sign;


import dnd.myOcean.service.member.SignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SignController {

    private final SignService signService;

    @GetMapping("/login/oauth2/callback/kakao")
    public ResponseEntity oauth2Login(@RequestParam("code") String code,
                                      @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String clientId,
                                      @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") String clientSecret,
                                      @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") String redirectUri) {
        return new ResponseEntity(code, HttpStatus.OK);
    }
}
