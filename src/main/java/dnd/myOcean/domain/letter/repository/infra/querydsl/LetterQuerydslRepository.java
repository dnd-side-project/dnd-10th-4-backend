package dnd.myOcean.domain.letter.repository.infra.querydsl;

import dnd.myOcean.domain.letter.dto.response.ReceivedLetterResponse;
import dnd.myOcean.domain.letter.dto.response.RepliedLetterResponse;
import dnd.myOcean.domain.letter.dto.response.SendLetterResponse;
import dnd.myOcean.domain.letter.repository.infra.querydsl.dto.LetterReadCondition;
import org.springframework.data.domain.Page;

public interface LetterQuerydslRepository {

    Page<SendLetterResponse> findAllSendLetter(LetterReadCondition cond);

    Page<ReceivedLetterResponse> findAllStoredLetter(LetterReadCondition cond);

    Page<RepliedLetterResponse> findAllReliedLetter(LetterReadCondition cond);
}
