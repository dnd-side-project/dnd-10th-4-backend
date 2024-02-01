package dnd.myOcean.domain.member.application;


import dnd.myOcean.domain.member.domain.Gender;
import dnd.myOcean.domain.member.domain.Member;
import dnd.myOcean.domain.member.domain.MemberWorry;
import dnd.myOcean.domain.member.domain.Worry;
import dnd.myOcean.domain.member.domain.WorryType;
import dnd.myOcean.domain.member.dto.request.BirthdayUpdateRequest;
import dnd.myOcean.domain.member.dto.request.GenderUpdateRequest;
import dnd.myOcean.domain.member.dto.request.MemberInfoRequest;
import dnd.myOcean.domain.member.dto.request.NicknameUpdateRequest;
import dnd.myOcean.domain.member.dto.request.WorryCreateRequest;
import dnd.myOcean.domain.member.dto.request.WorryDeleteRequest;
import dnd.myOcean.domain.member.dto.response.MemberInfoResponse;
import dnd.myOcean.domain.member.exception.AlreadyExistNicknameException;
import dnd.myOcean.domain.member.exception.BirthdayUpdateLimitExceedException;
import dnd.myOcean.domain.member.exception.GenderUpdateLimitExceedException;
import dnd.myOcean.domain.member.exception.MemberNotFoundException;
import dnd.myOcean.domain.member.exception.SameNicknameModifyRequestException;
import dnd.myOcean.domain.member.exception.WorrySelectionRangeLimitException;
import dnd.myOcean.domain.member.exception.WorryTypeContainsNotAccepted;
import dnd.myOcean.domain.member.repository.infra.jpa.MemberRepository;
import dnd.myOcean.domain.member.repository.infra.jpa.WorryRepository;
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
    public void updateAge(final BirthdayUpdateRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        if (member.isBirthDayChangeLimitExceeded()) {
            throw new BirthdayUpdateLimitExceedException();
        }

        member.updateAge(request.getBirthday());
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

        if (member.isNicknameEqualTo(request.getNickname())) {
            throw new SameNicknameModifyRequestException();
        }

        if (!isNicknameAvailable(request.getNickname())) {
            throw new AlreadyExistNicknameException();
        }

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

        member.setWorries(worries);
    }

    @Transactional
    public void deleteAllWorry(WorryDeleteRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);
        member.clearWorries();
    }

    public boolean isNicknameAvailable(String nickname) {
        return !memberRepository.existByNickName(nickname);
    }

    public MemberInfoResponse getMyInfo(MemberInfoRequest request) {
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
                member.getAge(),
                member.getRole().name());
    }
}
