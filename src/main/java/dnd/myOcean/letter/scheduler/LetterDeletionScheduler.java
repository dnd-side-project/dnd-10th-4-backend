package dnd.myOcean.letter.scheduler;

import dnd.myOcean.letter.repository.infra.jpa.LetterRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LetterDeletionScheduler {

    private final LetterRepository letterRepository;

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void deleteDiscardedLetters() {
        LocalDateTime expiredDate = LocalDateTime.now().minusHours(48);
        letterRepository.deleteDiscardedLetters(expiredDate);
    }
}
