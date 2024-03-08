package dnd.myOcean.member.application;


import dnd.myOcean.global.auth.aop.dto.CurrentMemberIdRequest;
import dnd.myOcean.member.domain.Gender;
import dnd.myOcean.member.domain.Member;
import dnd.myOcean.member.domain.Worry;
import dnd.myOcean.member.domain.WorryType;
import dnd.myOcean.member.domain.dto.request.BirthdayUpdateRequest;
import dnd.myOcean.member.domain.dto.request.GenderUpdateRequest;
import dnd.myOcean.member.domain.dto.request.InfoUpdateRequest;
import dnd.myOcean.member.domain.dto.request.NicknameUpdateRequest;
import dnd.myOcean.member.domain.dto.request.WorryCreateRequest;
import dnd.myOcean.member.domain.dto.response.MemberInfoResponse;
import dnd.myOcean.member.exception.AlreadyOnBoardingExecutedException;
import dnd.myOcean.member.exception.BirthdayUpdateLimitExceedException;
import dnd.myOcean.member.exception.GenderUpdateLimitExceedException;
import dnd.myOcean.member.exception.MemberNotFoundException;
import dnd.myOcean.member.exception.WorrySelectionRangeLimitException;
import dnd.myOcean.member.exception.WorryTypeContainsNotAccepted;
import dnd.myOcean.member.repository.infra.jpa.MemberRepository;
import dnd.myOcean.member.repository.infra.jpa.WorryRepository;
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
        validateWorryTypeSize(worryTypes, 1, 3);

        List<Worry> worries = worryTypes.stream()
                .map(worryType -> worryRepository.findByWorryType(worryType)
                        .orElseThrow(WorryTypeContainsNotAccepted::new))
                .collect(Collectors.toList());

        member.updateInfo(request, worries);
    }

    public MemberInfoResponse getMyInfo(CurrentMemberIdRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        return MemberInfoResponse.of(member);
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

        validateWorryTypeSize(worryTypes, 1, 3);

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

    @Transactional
    public void deleteMember(CurrentMemberIdRequest request) {
        memberRepository.deleteById(request.getMemberId());
    }
    
    private static void validateWorryTypeSize(List<WorryType> worryTypes, int minSize, int maxSize) {
        if (worryTypes.size() < minSize || worryTypes.size() > maxSize) {
            throw new WorrySelectionRangeLimitException();
        }
    }
}
