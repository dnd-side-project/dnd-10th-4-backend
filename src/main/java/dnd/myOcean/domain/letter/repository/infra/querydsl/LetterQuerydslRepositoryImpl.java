package dnd.myOcean.domain.letter.repository.infra.querydsl;

import static com.querydsl.core.types.Projections.constructor;
import static dnd.myOcean.domain.letter.domain.QLetter.letter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dnd.myOcean.domain.letter.domain.Letter;
import dnd.myOcean.domain.letter.domain.dto.response.ReceivedLetterResponse;
import dnd.myOcean.domain.letter.domain.dto.response.RepliedLetterResponse;
import dnd.myOcean.domain.letter.domain.dto.response.SendLetterResponse;
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
    public Page<SendLetterResponse> findAllSendLetter(LetterReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicateByCurrentMemberSend(cond);
        return new PageImpl<>(fetchAllSendLetter(predicate, pageable), pageable, fetchCount(predicate));
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

    private List<SendLetterResponse> fetchAllSendLetter(Predicate predicate, Pageable pageable) {
        return getQuerydsl().applyPagination(
                pageable,
                query.select(constructor(SendLetterResponse.class,
                                letter.createDate,
                                letter.id,
                                letter.sender.nickName,
                                letter.content,
                                letter.worryType))
                        .from(letter)
                        .where(predicate)
                        .orderBy(letter.createDate.asc())
        ).fetch();
    }

    @Override
    public Page<ReceivedLetterResponse> findAllStoredLetter(LetterReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicateByCurrentMemberStored(cond);
        return new PageImpl<>(fetchAllStoredLetter(predicate, pageable), pageable, fetchCount(predicate));
    }

    private Predicate createPredicateByCurrentMemberStored(LetterReadCondition cond) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!String.valueOf(cond.getMemberId()).isEmpty()) {
            builder.and(letter.receiver.id.eq(cond.getMemberId()));
            builder.and(letter.isStored.isTrue());
            return builder;
        }
        return builder;
    }

    private List<ReceivedLetterResponse> fetchAllStoredLetter(Predicate predicate, Pageable pageable) {
        return getQuerydsl().applyPagination(
                pageable,
                query.select(constructor(ReceivedLetterResponse.class,
                                letter.createDate,
                                letter.id,
                                letter.sender.nickName,
                                letter.receiver.nickName,
                                letter.content,
                                letter.worryType))
                        .from(letter)
                        .where(predicate)
                        .orderBy(letter.createDate.asc())
        ).fetch();
    }

    private Predicate createPredicateByCurrentMemberGetReplied(LetterReadCondition cond) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!String.valueOf(cond.getMemberId()).isEmpty()) {
            builder.and(letter.receiver.id.eq(cond.getMemberId()));
            builder.and(letter.hasReplied.isTrue());
            return builder;
        }
        return builder;
    }

    private List<RepliedLetterResponse> fetchAllRepliedLetter(Predicate predicate, Pageable pageable) {
        return getQuerydsl().applyPagination(
                pageable,
                query.select(constructor(RepliedLetterResponse.class,
                                letter.createDate,
                                letter.id,
                                letter.sender.nickName,
                                letter.receiver.nickName,
                                letter.content,
                                letter.replyContent,
                                letter.worryType))
                        .from(letter)
                        .where(predicate)
                        .orderBy(letter.createDate.asc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) {
        return query.select(letter.count()).from(letter).where(predicate).fetchOne();
    }
}
