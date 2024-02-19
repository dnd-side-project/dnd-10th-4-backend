package dnd.myOcean.member.repository.infra.jpa;

import dnd.myOcean.member.domain.Worry;
import dnd.myOcean.member.domain.WorryType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorryRepository extends JpaRepository<Worry, Long> {

    Optional<Worry> findByWorryType(WorryType worryType);
}
