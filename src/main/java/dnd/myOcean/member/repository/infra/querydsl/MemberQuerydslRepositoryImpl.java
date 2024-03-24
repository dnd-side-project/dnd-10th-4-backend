package dnd.myOcean.member.repository.infra.querydsl;

import static com.querydsl.core.types.Projections.constructor;
import static dnd.myOcean.member.domain.QMember.member;
import static dnd.myOcean.member.domain.QMemberWorry.memberWorry;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dnd.myOcean.member.domain.Member;
import dnd.myOcean.member.domain.dto.response.MemberInfoResponse;
import dnd.myOcean.member.repository.infra.querydsl.dto.MemberReadCondition;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class MemberQuerydslRepositoryImpl extends QuerydslRepositorySupport implements MemberQuerydslRepository {

    private final JPAQueryFactory query;

    public MemberQuerydslRepositoryImpl(JPAQueryFactory query) {
        super(Member.class);
        this.query = query;
    }

    @Override
    public Page<MemberInfoResponse> findAllMember(MemberReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAllMember(predicate, pageable), pageable, fetchCount(predicate));
    }

    private Predicate createPredicate(MemberReadCondition cond) {
        BooleanBuilder builder = new BooleanBuilder();
        if (cond.getEmail() != null) {
            builder.and(member.email.eq(cond.getEmail()));
            return builder;
        }
        return builder;
    }

    private List<MemberInfoResponse> fetchAllMember(Predicate predicate, Pageable pageable) {
        return getQuerydsl().applyPagination(
                pageable,
                query.select(constructor(MemberInfoResponse.class,
                                member.id,
                                member.role.stringValue(),
                                member.email,
                                member.nickName,
                                member.gender.stringValue(),
                                member.birthDay,
                                member.age))
                        .from(member)
                        .leftJoin(member.worries, memberWorry)
                        .where(predicate)
                        .orderBy(member.createDate.asc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) {
        return query.select(member.count())
                .from(member)
                .where(predicate)
                .fetchOne();
    }
}
