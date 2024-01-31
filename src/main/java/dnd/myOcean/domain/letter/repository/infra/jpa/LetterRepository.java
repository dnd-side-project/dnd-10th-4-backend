package dnd.myOcean.domain.letter.repository.infra.jpa;

import dnd.myOcean.domain.letter.domain.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Long> {
}
