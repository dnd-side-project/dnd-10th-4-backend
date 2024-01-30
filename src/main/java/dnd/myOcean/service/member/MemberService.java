package dnd.myOcean.service.member;


import dnd.myOcean.domain.member.Gender;
import dnd.myOcean.domain.member.Member;
import dnd.myOcean.domain.memberworry.MemberWorry;
import dnd.myOcean.domain.worry.Worry;
import dnd.myOcean.domain.worry.WorryType;
import dnd.myOcean.dto.member.request.MemberBirthdayUpdateRequest;
import dnd.myOcean.dto.member.request.MemberGenderUpdateRequest;
import dnd.myOcean.dto.member.request.MemberInfoRequest;
import dnd.myOcean.dto.member.request.MemberNicknameUpdateRequest;
import dnd.myOcean.dto.member.request.MemberWorryDeleteRequest;
import dnd.myOcean.dto.member.request.MemberWorryUpdateRequest;
import dnd.myOcean.dto.member.response.MemberInfoResponse;
import dnd.myOcean.exception.member.AlreadyExistNicknameException;
import dnd.myOcean.exception.member.BirthdayUpdateLimitExceedException;
import dnd.myOcean.exception.member.GenderUpdateLimitExceedException;
import dnd.myOcean.exception.member.MemberNotFoundException;
import dnd.myOcean.exception.member.SameNicknameModifyRequestException;
import dnd.myOcean.exception.member.WorrySelectionRangeLimitException;
import dnd.myOcean.exception.worry.WorryTypeContainsNotAccepted;
import dnd.myOcean.repository.jpa.member.MemberRepository;
import dnd.myOcean.repository.jpa.role.WorryRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final WorryRepository worryRepository;

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

        if (member.isNicknameEqualTo(memberNicknameUpdateRequest.getNickname())) {
            throw new SameNicknameModifyRequestException();
        }

        if (!isNicknameAvailable(memberNicknameUpdateRequest.getNickname())) {
            throw new AlreadyExistNicknameException();
        }

        member.updateNickname(memberNicknameUpdateRequest.getNickname());
    }

    @Transactional
    public void createWorry(final MemberWorryUpdateRequest memberWorryUpdateRequest) {
        Member member = memberRepository.findByEmail(memberWorryUpdateRequest.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        List<WorryType> worryTypes = memberWorryUpdateRequest.getWorries();

        if (worryTypes.size() < 1 || worryTypes.size() > 3) {
            throw new WorrySelectionRangeLimitException();
        }

        List<Worry> worries = worryTypes.stream()
                .map(worryType -> worryRepository.findByWorryType(worryType)
                        .orElseThrow(WorryTypeContainsNotAccepted::new))
                .collect(Collectors.toList());

        member.updateWorries(worries);
    }

    @Transactional
    public void deleteWorry(MemberWorryDeleteRequest memberWorryDeleteRequest) {
        Member member = memberRepository.findByEmail(memberWorryDeleteRequest.getEmail())
                .orElseThrow(MemberNotFoundException::new);
        member.clearWorries();
    }

    public boolean isNicknameAvailable(String nickname) {
        Optional<Member> existingMember = memberRepository.findByNickName(nickname);
        return existingMember.isEmpty();
    }

    public MemberInfoResponse getMyInfo(MemberInfoRequest memberInfoRequest) {
        Member member = memberRepository.findByEmail(memberInfoRequest.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        List<MemberWorry> memberWorries = member.getWorries();

        List<WorryType> worryTypes = memberWorries.stream().map(memberWorry -> memberWorry.getWorry().getWorryType())
                .collect(Collectors.toList());

        return new MemberInfoResponse(member.getId(),
                member.getEmail(),
                member.getNickName(),
                worryTypes,
                member.getGender().name(),
                member.getAge(),
                member.getRole().name());
    }
}

