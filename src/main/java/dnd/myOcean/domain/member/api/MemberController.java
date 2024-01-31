package dnd.myOcean.domain.member.api;


import dnd.myOcean.domain.member.application.MemberService;
import dnd.myOcean.domain.member.dto.request.BirthdayUpdateRequest;
import dnd.myOcean.domain.member.dto.request.GenderUpdateRequest;
import dnd.myOcean.domain.member.dto.request.MemberInfoRequest;
import dnd.myOcean.domain.member.dto.request.NicknameUpdateRequest;
import dnd.myOcean.domain.member.dto.request.WorryCreateRequest;
import dnd.myOcean.domain.member.dto.request.WorryDeleteRequest;
import dnd.myOcean.global.auth.aop.AssignCurrentMemberId;
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
    @AssignCurrentMemberId
    public ResponseEntity updateBirthday(@RequestBody BirthdayUpdateRequest request) {
        memberService.updateAge(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/gender")
    @AssignCurrentMemberId
    public ResponseEntity updateGender(@RequestBody GenderUpdateRequest request) {
        memberService.updateGender(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/nickname")
    @AssignCurrentMemberId
    public ResponseEntity updateNickname(@RequestBody NicknameUpdateRequest request) {
        memberService.updateNickname(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/worry")
    @AssignCurrentMemberId
    public ResponseEntity createWorry(@RequestBody WorryCreateRequest request) {
        memberService.createWorry(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/worry")
    @AssignCurrentMemberId
    public ResponseEntity deleteAllWorry(@RequestBody WorryDeleteRequest request) {
        memberService.deleteAllWorry(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    @AssignCurrentMemberId
    public ResponseEntity getMyInfo(@RequestBody MemberInfoRequest request) {
        return new ResponseEntity(memberService.getMyInfo(request), HttpStatus.OK);
    }
}
