package dnd.myOcean.member.application;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import dnd.myOcean.global.auth.aop.dto.CurrentMemberIdRequest;
import dnd.myOcean.member.application.MemberService;
import dnd.myOcean.member.domain.Gender;
import dnd.myOcean.member.domain.Member;
import dnd.myOcean.member.domain.Role;
import dnd.myOcean.member.domain.Worry;
import dnd.myOcean.member.domain.WorryType;
import dnd.myOcean.member.domain.dto.request.InfoUpdateRequest;
import dnd.myOcean.member.domain.dto.response.MemberInfoResponse;
import dnd.myOcean.member.repository.infra.jpa.MemberRepository;
import dnd.myOcean.member.repository.infra.jpa.WorryRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WorryRepository worryRepository;

    private Member member;
    private Worry worry;

    @BeforeEach
    void initMember() {
        member = Member.builder()
                .id(1L)
                .email("tlswodnjs@email.com")
                .role(Role.USER)
                .nickName(null)
                .birthDay(LocalDate.now())
                .age(12)
                .gender(Gender.NONE)
                .updateAgeCount(0)
                .updateGenderCount(0)
                .letterCount(0)
                .build();
    }

    @BeforeEach
    void initWorry() {
//        worry = Worry.builder()
//                .worryType(WorryType.WORK)
//                .build();
    }

    @Test
    @DisplayName("유저 정보 수정")
    void member_update_info() {
        InfoUpdateRequest infoUpdateRequest =
                new InfoUpdateRequest(1L, "고양이",
                        "1999-12-25", Gender.MALE.name(), List.of(WorryType.WORK.name()));

        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        given(worryRepository.findByWorryType(WorryType.WORK)).willReturn(Optional.of(worry));

        memberService.updateInfo(infoUpdateRequest);

        then(member.getBirthDay()).isEqualTo(infoUpdateRequest.getBirthday());
        then(member.getNickName()).contains("고양이");
        then(member.getGender().name()).isEqualTo(Gender.MALE.name());
        then(member.getWorries().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("유저 정보 조회")
    void member_check_info() {
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        CurrentMemberIdRequest request = new CurrentMemberIdRequest(1L);

        MemberInfoResponse response = memberService.getMyInfo(request);

        then(response).isNotNull();
        then(response.getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("유저 고민 삭제")
    void member_delete_worry() {
        given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        CurrentMemberIdRequest request = new CurrentMemberIdRequest(1L);

        memberService.deleteAllWorry(request);

        then(member.getWorries()).isEmpty();
    }


    @Test
    @DisplayName("유저 탈퇴")
    void member_leave() {
        /*given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
        CurrentMemberIdRequest request = new CurrentMemberIdRequest(1L);

        memberService.deleteMember(request);

        verify(memberRepository, times(1)).deleteById(request.getMemberId());*/
    }
}