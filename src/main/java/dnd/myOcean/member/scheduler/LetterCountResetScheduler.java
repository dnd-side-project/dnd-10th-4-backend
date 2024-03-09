package dnd.myOcean.member.scheduler;

import dnd.myOcean.member.domain.Member;
import dnd.myOcean.member.repository.infra.jpa.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LetterCountResetScheduler {

    private final MemberRepository memberRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetLetterCount() {
        List<Member> members = memberRepository.findAll();
        members.forEach(member -> member.resetLetterCount(5));
    }
}
