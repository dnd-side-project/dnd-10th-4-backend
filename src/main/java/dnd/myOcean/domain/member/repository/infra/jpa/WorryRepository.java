package dnd.myOcean.domain.member.repository.infra.jpa;

import dnd.myOcean.domain.member.domain.Worry;
import dnd.myOcean.domain.member.domain.WorryType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorryRepository extends JpaRepository<Worry, Long> {

    Optional<Worry> findByWorryType(WorryType worryType);
}
