package dnd.myOcean.service.member;

import dnd.myOcean.domain.member.Member;
import dnd.myOcean.dto.oAuth.response.MemberInfo;
import dnd.myOcean.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public void createMember(MemberInfo memberInfo) {
        memberRepository.save(Member
                .builder()
                .username(memberInfo.getNickName())
                .nickname(null)
                .build());
    }
}
