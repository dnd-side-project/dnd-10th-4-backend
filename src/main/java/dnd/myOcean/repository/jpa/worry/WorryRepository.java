package dnd.myOcean.repository.jpa.worry;

import dnd.myOcean.domain.worry.Worry;
import dnd.myOcean.domain.worry.WorryType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorryRepository extends JpaRepository<Worry, Long> {

    Optional<Worry> findByWorryType(WorryType worryType);
}
