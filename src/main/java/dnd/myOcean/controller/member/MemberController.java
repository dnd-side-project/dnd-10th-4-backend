package dnd.myOcean.controller.member;


import dnd.myOcean.aop.AssignCurrentEmail;
import dnd.myOcean.dto.member.request.MemberBirthdayUpdateRequest;
import dnd.myOcean.dto.member.request.MemberGenderUpdateRequest;
import dnd.myOcean.dto.member.request.MemberInfoRequest;
import dnd.myOcean.dto.member.request.MemberNicknameUpdateRequest;
import dnd.myOcean.dto.member.request.MemberWorryDeleteRequest;
import dnd.myOcean.dto.member.request.MemberWorryUpdateRequest;
import dnd.myOcean.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PatchMapping("/nickname")
    @AssignCurrentEmail
    public ResponseEntity updateNickname(@RequestBody MemberNicknameUpdateRequest memberNicknameUpdateRequest) {
        memberService.updateNickname(memberNicknameUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/worry")
    @AssignCurrentEmail
    public ResponseEntity createWorry(@RequestBody MemberWorryUpdateRequest memberWorryUpdateRequest) {
        memberService.createWorry(memberWorryUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/worry")
    @AssignCurrentEmail
    public ResponseEntity deleteWorry(@RequestBody MemberWorryDeleteRequest memberWorryDeleteRequest) {
        memberService.deleteWorry(memberWorryDeleteRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    @AssignCurrentEmail
    public ResponseEntity getMyInfo(@RequestBody MemberInfoRequest memberInfoRequest) {
        return new ResponseEntity(memberService.getMyInfo(memberInfoRequest), HttpStatus.OK);
    }
}
