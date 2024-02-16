package dnd.myOcean.domain.member.application;


import dnd.myOcean.domain.member.domain.Gender;
import dnd.myOcean.domain.member.domain.Member;
import dnd.myOcean.domain.member.domain.MemberWorry;
import dnd.myOcean.domain.member.domain.Worry;
import dnd.myOcean.domain.member.domain.WorryType;
import dnd.myOcean.domain.member.domain.dto.request.BirthdayUpdateRequest;
import dnd.myOcean.domain.member.domain.dto.request.GenderUpdateRequest;
import dnd.myOcean.domain.member.domain.dto.request.InfoUpdateRequest;
import dnd.myOcean.domain.member.domain.dto.request.NicknameUpdateRequest;
import dnd.myOcean.domain.member.domain.dto.request.WorryCreateRequest;
import dnd.myOcean.domain.member.domain.dto.response.MemberInfoResponse;
import dnd.myOcean.domain.member.exception.AlreadyOnBoardingExecutedException;
import dnd.myOcean.domain.member.exception.BirthdayUpdateLimitExceedException;
import dnd.myOcean.domain.member.exception.GenderUpdateLimitExceedException;
import dnd.myOcean.domain.member.exception.MemberNotFoundException;
import dnd.myOcean.domain.member.exception.WorrySelectionRangeLimitException;
import dnd.myOcean.domain.member.exception.WorryTypeContainsNotAccepted;
import dnd.myOcean.domain.member.repository.infra.jpa.MemberRepository;
import dnd.myOcean.domain.member.repository.infra.jpa.WorryRepository;
import dnd.myOcean.global.auth.aop.dto.CurrentMemberIdRequest;
import java.util.List;
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
    public void updateInfo(final InfoUpdateRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        if (!member.isFirstLogin()) {
            throw new AlreadyOnBoardingExecutedException();
        }

        List<WorryType> worryTypes = request.getWorries();
        if (worryTypes.size() < 1 || worryTypes.size() > 3) {
            throw new WorrySelectionRangeLimitException();
        }

        List<Worry> worries = worryTypes.stream()
                .map(worryType -> worryRepository.findByWorryType(worryType)
                        .orElseThrow(WorryTypeContainsNotAccepted::new))
                .collect(Collectors.toList());

        member.updateInfo(request, worries);
    }

    @Transactional
    public void updateBirthday(final BirthdayUpdateRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        if (member.isBirthDayChangeLimitExceeded()) {
            throw new BirthdayUpdateLimitExceedException();
        }

        member.updateBirthday(request.getBirthday());
    }

    @Transactional
    public void updateGender(final GenderUpdateRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        if (member.isGenderChangeLimitExceeded()) {
            throw new GenderUpdateLimitExceedException();
        }

        member.updateGender(Gender.from(request.getGender()));
    }

    @Transactional
    public void updateNickname(final NicknameUpdateRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        member.updateNickname(request.getNickname());
    }

    @Transactional
    public void createWorry(final WorryCreateRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        List<WorryType> worryTypes = request.getWorries();

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
    public void deleteAllWorry(CurrentMemberIdRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        member.clearWorries();
    }

    public MemberInfoResponse getMyInfo(CurrentMemberIdRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
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
                member.getBirthDay(),
                member.getAge(),
                member.getRole().name());
    }

    @Transactional
    public void deleteMember(CurrentMemberIdRequest request) {
        memberRepository.deleteById(request.getMemberId());
    }
}
