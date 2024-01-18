package dnd.myOcean.service.member;

import dnd.myOcean.domain.member.Member;
import dnd.myOcean.dto.sign.MemberCreateRequest;
import dnd.myOcean.dto.sign.MemberLoginRequest;
import dnd.myOcean.exception.MemberNotFoundException;
import dnd.myOcean.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void join(MemberCreateRequest memberCreateRequest) {
        Member member = new Member().builder()
                .username(memberCreateRequest.getUsername())
                .nickname(memberCreateRequest.getNickname())
                .password(passwordEncoder.encode(memberCreateRequest.getPassword()))
                .build();

        memberRepository.save(member);
    }

    public void login(MemberLoginRequest memberLoginRequest) {
        memberRepository.findByUsername(memberLoginRequest.getUsername())
                .orElseThrow(MemberNotFoundException::new);
    }
}
