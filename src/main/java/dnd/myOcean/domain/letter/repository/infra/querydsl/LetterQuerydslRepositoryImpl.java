package dnd.myOcean.domain.letter.repository.infra.querydsl;

import static com.querydsl.core.types.Projections.constructor;
import static dnd.myOcean.domain.letter.domain.QLetter.letter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dnd.myOcean.domain.letter.domain.Letter;
import dnd.myOcean.domain.letter.dto.response.LetterResponse;
import dnd.myOcean.domain.letter.repository.infra.querydsl.dto.LetterReadCondition;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class LetterQuerydslRepositoryImpl extends QuerydslRepositorySupport implements LetterQuerydslRepository {

    private final JPAQueryFactory query;

    public LetterQuerydslRepositoryImpl(JPAQueryFactory query) {
        super(Letter.class);
        this.query = query;
    }

    @Override
    public Page<LetterResponse> findAllSendLetter(LetterReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicateByCurrentMemberSend(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }

    @Override
    public Page<LetterResponse> findAllReceiveLetter(LetterReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicateByCurrentMemberReceive(cond);
        return new PageImpl<>(fetchAll(predicate, pageable), pageable, fetchCount(predicate));
    }

    private Predicate createPredicateByCurrentMemberSend(LetterReadCondition cond) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!String.valueOf(cond.getMemberId()).isEmpty()) {
            builder.and(letter.sender.id.eq(cond.getMemberId()));
            builder.and(letter.isDeleteBySender.isFalse());
            return builder;
        }
        return builder;
    }

    private Predicate createPredicateByCurrentMemberReceive(LetterReadCondition cond) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!String.valueOf(cond.getMemberId()).isEmpty()) {
            builder.and(letter.receiver.id.eq(cond.getMemberId()));
            builder.and(letter.isDeleteByReceiver.isFalse());
            return builder;
        }
        return builder;
    }

    private List<LetterResponse> fetchAll(Predicate predicate, Pageable pageable) {
        return getQuerydsl().applyPagination(
                pageable,
                query.select(constructor(LetterResponse.class,
                                letter.id,
                                letter.sender.nickName,
                                letter.receiver.nickName,
                                letter.content,
                                letter.worryType,
                                letter.isRead))
                        .from(letter)
                        .where(predicate)
                        .orderBy(letter.createDate.asc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) {
        return query.select(letter.count()).from(letter).where(predicate).fetchOne();
    }
}
