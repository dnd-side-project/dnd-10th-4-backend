package dnd.myOcean.domain.member.application;


import dnd.myOcean.domain.member.domain.Gender;
import dnd.myOcean.domain.member.domain.Member;
import dnd.myOcean.domain.member.domain.MemberWorry;
import dnd.myOcean.domain.member.domain.Worry;
import dnd.myOcean.domain.member.domain.WorryType;
import dnd.myOcean.domain.member.dto.request.MemberBirthdayUpdateRequest;
import dnd.myOcean.domain.member.dto.request.MemberGenderUpdateRequest;
import dnd.myOcean.domain.member.dto.request.MemberInfoRequest;
import dnd.myOcean.domain.member.dto.request.MemberNicknameUpdateRequest;
import dnd.myOcean.domain.member.dto.request.MemberWorryCreateRequest;
import dnd.myOcean.domain.member.dto.request.MemberWorryDeleteRequest;
import dnd.myOcean.domain.member.dto.response.MemberInfoResponse;
import dnd.myOcean.domain.member.exception.exceptions.AlreadyExistNicknameException;
import dnd.myOcean.domain.member.exception.exceptions.BirthdayUpdateLimitExceedException;
import dnd.myOcean.domain.member.exception.exceptions.GenderUpdateLimitExceedException;
import dnd.myOcean.domain.member.exception.exceptions.MemberNotFoundException;
import dnd.myOcean.domain.member.exception.exceptions.SameNicknameModifyRequestException;
import dnd.myOcean.domain.member.exception.exceptions.WorrySelectionRangeLimitException;
import dnd.myOcean.domain.member.exception.exceptions.WorryTypeContainsNotAccepted;
import dnd.myOcean.domain.member.repository.infra.jpa.MemberRepository;
import dnd.myOcean.domain.member.repository.infra.jpa.WorryRepository;
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
    public void createWorry(final MemberWorryCreateRequest memberWorryCreateRequest) {
        Member member = memberRepository.findByEmail(memberWorryCreateRequest.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        List<WorryType> worryTypes = memberWorryCreateRequest.getWorries();

        if (worryTypes.size() < 1 || worryTypes.size() > 3) {
            throw new WorrySelectionRangeLimitException();
        }

        List<Worry> worries = worryTypes.stream()
                .map(worryType -> worryRepository.findByWorryType(worryType)
                        .orElseThrow(WorryTypeContainsNotAccepted::new))
                .collect(Collectors.toList());

        member.setWorries(worries);
    }

    @Transactional
    public void deleteAllWorry(MemberWorryDeleteRequest memberWorryDeleteRequest) {
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

        List<WorryType> worryTypes = memberWorries.stream()
                .map(memberWorry -> memberWorry.getWorry().getWorryType())
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

