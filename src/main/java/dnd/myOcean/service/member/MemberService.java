package dnd.myOcean.service.member;


import dnd.myOcean.domain.member.*;
import dnd.myOcean.dto.member.*;
import dnd.myOcean.dto.member.response.MemberInfoResponse;
import dnd.myOcean.exception.member.*;
import dnd.myOcean.repository.jpa.member.MemberRepository;
import dnd.myOcean.repository.jpa.member.MemberWorryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberWorryRepository memberWorryRepository;

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
    public void updateWorry(final MemberWorryUpdateRequest memberWorryUpdateRequest) {
        Member member = memberRepository.findByEmail(memberWorryUpdateRequest.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        List<WorryType> worries = memberWorryUpdateRequest.getWorries();

        if (worries.size() < 1 || worries.size() > 3) {
            throw new WorrySelectionRangeLimitException();
        }

        memberWorryRepository.deleteByMember(member);

        worries.stream()
                .map(Worry::createWorry)
                .map(worry -> MemberWorry.builder().member(member).worry(worry).build())
                .forEach(memberWorryRepository::save);
    }

    public boolean isNicknameAvailable(String nickname) {
        Optional<Member> existingMember = memberRepository.findByNickName(nickname);
        return existingMember.isEmpty();
    }

    public MemberInfoResponse getMyInfo(MemberInfoRequest memberInfoRequest) {
        Member member = memberRepository.findByEmail(memberInfoRequest.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        return new MemberInfoResponse(member.getId(),
                member.getEmail(),
                member.getNickName(),
                member.getGender().name(),
                member.getAge(),
                member.getRole().name());
    }
}

