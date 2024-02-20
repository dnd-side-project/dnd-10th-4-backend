package dnd.myOcean.member.api;

import dnd.myOcean.global.auth.aop.AssignCurrentMemberId;
import dnd.myOcean.global.auth.aop.dto.CurrentMemberIdRequest;
import dnd.myOcean.member.application.MemberService;
import dnd.myOcean.member.domain.dto.request.BirthdayUpdateRequest;
import dnd.myOcean.member.domain.dto.request.GenderUpdateRequest;
import dnd.myOcean.member.domain.dto.request.InfoUpdateRequest;
import dnd.myOcean.member.domain.dto.request.NicknameUpdateRequest;
import dnd.myOcean.member.domain.dto.request.WorryCreateRequest;
import dnd.myOcean.member.domain.dto.response.MemberInfoResponse;
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

    @GetMapping
    @AssignCurrentMemberId
    public ResponseEntity<MemberInfoResponse> getMyInfo(CurrentMemberIdRequest request) {
        return new ResponseEntity(memberService.getMyInfo(request), HttpStatus.OK);
    }

    @PatchMapping
    @AssignCurrentMemberId
    public ResponseEntity<Void> updateInfo(@RequestBody InfoUpdateRequest request) {
        memberService.updateInfo(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/birthday")
    @AssignCurrentMemberId
    public ResponseEntity<Void> updateBirthday(@RequestBody BirthdayUpdateRequest request) {
        memberService.updateBirthday(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/gender")
    @AssignCurrentMemberId
    public ResponseEntity<Void> updateGender(@RequestBody GenderUpdateRequest request) {
        memberService.updateGender(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/nickname")
    @AssignCurrentMemberId
    public ResponseEntity<Void> updateNickname(@RequestBody NicknameUpdateRequest request) {
        memberService.updateNickname(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/worry")
    @AssignCurrentMemberId
    public ResponseEntity<Void> createWorry(@RequestBody WorryCreateRequest request) {
        memberService.createWorry(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/worry")
    @AssignCurrentMemberId
    public ResponseEntity<Void> deleteAllWorry(CurrentMemberIdRequest request) {
        memberService.deleteAllWorry(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/sign-out")
    @AssignCurrentMemberId
    public ResponseEntity<Void> deleteMember(CurrentMemberIdRequest request) {
        memberService.deleteMember(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
