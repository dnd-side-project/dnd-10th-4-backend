package dnd.myOcean.repository.jpa.member;

import dnd.myOcean.domain.member.Member;
import dnd.myOcean.domain.member.MemberWorry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberWorryRepository extends JpaRepository<MemberWorry, Long> {

    void deleteByMember(Member member);
}
