package dnd.myOcean.letterimage.repository.infra.jpa;

import dnd.myOcean.letterimage.domain.LetterImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LetterImageRepository extends JpaRepository<LetterImage, Long> {

    @Query("SELECT li FROM LetterImage li WHERE li.originName = 'oceanLetter.jpeg'")
    Optional<LetterImage> findOnboardingLetter();
}
