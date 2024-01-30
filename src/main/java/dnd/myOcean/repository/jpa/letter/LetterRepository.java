package dnd.myOcean.repository.jpa.letter;

import dnd.myOcean.domain.letter.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Long> {
}
