package dnd.myOcean.service.member;


import dnd.myOcean.domain.member.Member;
import dnd.myOcean.dto.member.MemberBirthdayUpdateRequest;
import dnd.myOcean.dto.member.MemberGenderUpdateRequest;
import dnd.myOcean.exception.member.AlreadyUpdatedBirthdayException;
import dnd.myOcean.exception.member.AlreadyUpdatedGenderException;
import dnd.myOcean.exception.member.MemberNotFoundException;
import dnd.myOcean.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void updateBirthday(final String email, final MemberBirthdayUpdateRequest memberBirthdayUpdateRequest) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        if (member.isAlreadyUpdateBirthday()) {
            throw new AlreadyUpdatedBirthdayException();
        }

        member.updateBirthday(memberBirthdayUpdateRequest.getBirthday());
    }

    @Transactional
    public void updateGender(final String email, final MemberGenderUpdateRequest memberGenderUpdateRequest) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        if (member.isAlreadyUpdateGender()) {
            throw new AlreadyUpdatedGenderException();
        }

        if (member.isGenderEqualToRequest(memberGenderUpdateRequest.getGender())) {
            member.updateGender(memberGenderUpdateRequest.getGender(), false);
            return;
        }

        member.updateGender(memberGenderUpdateRequest.getGender(), true);
    }
}

