package dnd.myOcean.service.member;


import dnd.myOcean.domain.member.Gender;
import dnd.myOcean.domain.member.Member;
import dnd.myOcean.domain.member.Worry;
import dnd.myOcean.dto.member.MemberBirthdayUpdateRequest;
import dnd.myOcean.dto.member.MemberGenderUpdateRequest;
import dnd.myOcean.dto.member.MemberNicknameUpdateRequest;
import dnd.myOcean.dto.member.MemberWorryUpdateRequest;
import dnd.myOcean.exception.member.*;
import dnd.myOcean.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void updateAge(final String email, final MemberBirthdayUpdateRequest memberBirthdayUpdateRequest) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        if (member.isBirthDayChangeLimitExceeded()) {
            throw new BirthdayUpdateLimitExceedException();
        }

        member.updateAge(memberBirthdayUpdateRequest.getBirthday());
    }

    @Transactional
    public void updateGender(final String email, final MemberGenderUpdateRequest memberGenderUpdateRequest) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        if (member.isGenderChangeLimitExceeded()) {
            throw new GenderUpdateLimitExceedException();
        }

        member.updateGender(Gender.from(memberGenderUpdateRequest.getGender()));
    }

    @Transactional
    public void updateNickname(final String email, final MemberNicknameUpdateRequest memberNicknameUpdateRequest) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        if(member.isNicknameChangeLimitExceeded()){
            throw new NicknameUpdateLimitExceedException();
        }

        member.updateNickname(memberNicknameUpdateRequest.getNickname());
    }

    @Transactional
    public void updateWorry(final String email, final MemberWorryUpdateRequest memberWorryUpdateRequest) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        List<Worry> worries = memberWorryUpdateRequest.getWorries();

        if(worries.size() > 3) {
            throw new WorryUpdateLimitExceedException();
        }

        member.updateWorry(worries);
    }
}

