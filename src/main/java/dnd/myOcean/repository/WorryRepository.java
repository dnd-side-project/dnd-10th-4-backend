package dnd.myOcean.repository;

import dnd.myOcean.domain.member.Member;
import dnd.myOcean.domain.member.Worry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorryRepository extends JpaRepository<Worry, Long> {

    void deleteByMember(Member member);
}
