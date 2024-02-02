package dnd.myOcean.domain.letter.repository.infra.querydsl;

import dnd.myOcean.domain.letter.dto.response.LetterResponse;
import dnd.myOcean.domain.letter.repository.infra.querydsl.dto.LetterReadCondition;
import org.springframework.data.domain.Page;

public interface LetterQuerydslRepository {

    Page<LetterResponse> findAllSendLetter(LetterReadCondition cond);

    Page<LetterResponse> findAllStoredLetter(LetterReadCondition cond);

    Page<LetterResponse> findAllReliedLetter(LetterReadCondition cond);
}
