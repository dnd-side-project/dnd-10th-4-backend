package dnd.myOcean.domain.letter.repository.infra.querydsl;

import dnd.myOcean.domain.letter.domain.dto.response.ReceivedLetterResponse;
import dnd.myOcean.domain.letter.domain.dto.response.RepliedLetterResponse;
import dnd.myOcean.domain.letter.domain.dto.response.SendLetterResponse;
import dnd.myOcean.domain.letter.repository.infra.querydsl.dto.LetterReadCondition;
import org.springframework.data.domain.Page;

public interface LetterQuerydslRepository {

    Page<SendLetterResponse> findAllSendLetter(LetterReadCondition cond);

    Page<ReceivedLetterResponse> findAllStoredLetter(LetterReadCondition cond);

    Page<RepliedLetterResponse> findAllReliedLetter(LetterReadCondition cond);
}
