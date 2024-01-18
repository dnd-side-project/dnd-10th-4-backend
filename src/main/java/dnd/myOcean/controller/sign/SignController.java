package dnd.myOcean.controller.sign;


import static org.springframework.http.HttpStatus.OK;

import dnd.myOcean.dto.sign.MemberCreateRequest;
import dnd.myOcean.dto.sign.MemberLoginRequest;
import dnd.myOcean.service.member.SignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SignController {

    private final SignService signService;

    @PostMapping("/join")
    public ResponseEntity join(@Valid @RequestBody MemberCreateRequest memberCreateRequest) {
        signService.join(memberCreateRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody MemberLoginRequest memberLoginRequest) {
        signService.login(memberLoginRequest);
        return new ResponseEntity(OK);
    }

    @GetMapping("/login/oauth2/callback/kakao")
    public ResponseEntity oauth2Login(@RequestParam("code") String code,
                                      @Value("${spring.security.oauth2.client.registration.kakao.client-id}") String clientId,
                                      @Value("${spring.security.oauth2.client.registration.kakao.client-secret}") String clientSecret,
                                      @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}") String redirectUri) {
        System.out.println(code);
        return new ResponseEntity(OK);
    }
}
