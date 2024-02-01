package dnd.myOcean.domain.letter.repository.infra.jpa;

import dnd.myOcean.domain.letter.domain.Letter;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface LetterRepository extends JpaRepository<Letter, Long> {

    Optional<Letter> findByIdAndSenderId(@Param("id") Long id, @Param("senderId") Long senderId);

    Optional<Letter> findByIdAndReceiverId(@Param("id") Long id, @Param("senderId") Long receiverId);
}
