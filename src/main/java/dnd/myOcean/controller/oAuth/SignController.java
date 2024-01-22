package dnd.myOcean.controller.oAuth;

import dnd.myOcean.domain.member.Member;
import dnd.myOcean.dto.member.request.MemberBirthdayRequest;
import dnd.myOcean.dto.member.request.MemberGenderRequest;
import dnd.myOcean.dto.member.response.MemberResponseDto;
import dnd.myOcean.service.sign.KakaoMemberDetailsService;
import dnd.myOcean.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class SignController {

    private final TokenService tokenService;
    private final KakaoMemberDetailsService kakaoMemberDetailsService;

    @GetMapping("/login/oauth2/code/kakao")
    public ResponseEntity kakaoCallback(@RequestParam("code") String code) {
        return new ResponseEntity(code, HttpStatus.OK);
    }

    @PatchMapping("/login/oauth2/{memberId}/birthday")
    public ResponseEntity updateBirthday(@PathVariable("memberId") Long memberId,
                                         @RequestBody MemberBirthdayRequest memberBirthday) {

        Member member = kakaoMemberDetailsService.updateBirthday(memberId, memberBirthday);
        MemberResponseDto memberResponseDto = new MemberResponseDto(member);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/login/oauth2/{memberId}/gender")
    public ResponseEntity updateGender(@PathVariable("memberId") Long memberId,
                                       @RequestBody MemberGenderRequest memberGender) {

        Member member = kakaoMemberDetailsService.updateGender(memberId, memberGender);
        MemberResponseDto memberResponseDto = new MemberResponseDto(member);
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }
}
