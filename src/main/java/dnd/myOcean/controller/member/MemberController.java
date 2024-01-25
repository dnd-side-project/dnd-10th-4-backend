package dnd.myOcean.controller.member;


import dnd.myOcean.aop.AssignCurrentEmail;
import dnd.myOcean.dto.member.MemberBirthdayUpdateRequest;
import dnd.myOcean.dto.member.MemberGenderUpdateRequest;
import dnd.myOcean.dto.member.MemberNicknameUpdateRequest;
import dnd.myOcean.dto.member.MemberWorryUpdateRequest;
import dnd.myOcean.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static dnd.myOcean.config.security.util.SecurityUtils.getCurrentEmail;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/birthday")
    @AssignCurrentEmail
    public ResponseEntity updateBirthday(@RequestBody MemberBirthdayUpdateRequest memberBirthdayUpdateRequest) {
        memberService.updateAge(memberBirthdayUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/gender")
    @AssignCurrentEmail
    public ResponseEntity updateGender(@RequestBody MemberGenderUpdateRequest memberGenderUpdateRequest) {
        memberService.updateGender(memberGenderUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/nick-name")
    public ResponseEntity updateNickname(@RequestBody MemberNicknameUpdateRequest memberNicknameUpdateRequest) {
        memberService.updateNickname(getCurrentEmail(), memberNicknameUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/worry")
    public ResponseEntity updateWorry(@RequestBody MemberWorryUpdateRequest memberWorryUpdateRequest) {
        memberService.updateWorry(getCurrentEmail(), memberWorryUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
