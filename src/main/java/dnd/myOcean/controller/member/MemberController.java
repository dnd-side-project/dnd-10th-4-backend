package dnd.myOcean.controller.member;


import static dnd.myOcean.config.security.util.SecurityUtils.getCurrentEmail;

import dnd.myOcean.dto.member.MemberBirthdayUpdateRequest;
import dnd.myOcean.dto.member.MemberGenderUpdateRequest;
import dnd.myOcean.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/birthday")
    public ResponseEntity updateBirthday(@RequestBody MemberBirthdayUpdateRequest memberBirthdayUpdateRequest) {
        memberService.updateAge(getCurrentEmail(), memberBirthdayUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/gender")
    public ResponseEntity updateGender(@RequestBody MemberGenderUpdateRequest memberGenderUpdateRequest) {
        memberService.updateGender(getCurrentEmail(), memberGenderUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
