package dnd.myOcean.service.member;


import dnd.myOcean.domain.member.Gender;
import dnd.myOcean.domain.member.Member;
import dnd.myOcean.domain.member.Worry;
import dnd.myOcean.dto.member.MemberBirthdayUpdateRequest;
import dnd.myOcean.dto.member.MemberGenderUpdateRequest;
import dnd.myOcean.dto.member.MemberNicknameUpdateRequest;
import dnd.myOcean.dto.member.MemberWorryUpdateRequest;
import dnd.myOcean.exception.member.AlreadyExistNicknameException;
import dnd.myOcean.exception.member.BirthdayUpdateLimitExceedException;
import dnd.myOcean.exception.member.GenderUpdateLimitExceedException;
import dnd.myOcean.exception.member.MaxWorrySelectionLimitException;
import dnd.myOcean.exception.member.MemberNotFoundException;
import dnd.myOcean.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void updateAge(final MemberBirthdayUpdateRequest memberBirthdayUpdateRequest) {
        Member member = memberRepository.findByEmail(memberBirthdayUpdateRequest.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        if (member.isBirthDayChangeLimitExceeded()) {
            throw new BirthdayUpdateLimitExceedException();
        }

        member.updateAge(memberBirthdayUpdateRequest.getBirthday());
    }

    @Transactional
    public void updateGender(final MemberGenderUpdateRequest memberGenderUpdateRequest) {
        Member member = memberRepository.findByEmail(memberGenderUpdateRequest.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        if (member.isGenderChangeLimitExceeded()) {
            throw new GenderUpdateLimitExceedException();
        }

        member.updateGender(Gender.from(memberGenderUpdateRequest.getGender()));
    }

    @Transactional
    public void updateNickname(final MemberNicknameUpdateRequest memberNicknameUpdateRequest) {
        Member member = memberRepository.findByEmail(memberNicknameUpdateRequest.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        if (!isNicknameAvailable(memberNicknameUpdateRequest.getNickname())) {
            throw new AlreadyExistNicknameException();
        }

        member.updateNickname(memberNicknameUpdateRequest.getNickname());
    }

    @Transactional
    public void updateWorry(final MemberWorryUpdateRequest memberWorryUpdateRequest) {
        Member member = memberRepository.findByEmail(memberWorryUpdateRequest.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        List<Worry> worries = memberWorryUpdateRequest.getWorries();

        if (worries.size() > 3) {
            throw new MaxWorrySelectionLimitException();
        }

        member.updateWorry(worries);
    }

    public boolean isNicknameAvailable(String nickname) {
        Optional<Member> existingMember = memberRepository.findByNickname(nickname);
        return existingMember.isEmpty();
    }
}

