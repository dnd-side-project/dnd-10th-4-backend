package dnd.myOcean.report.repository.infra.querydsl;

import static com.querydsl.core.types.Projections.constructor;
import static dnd.myOcean.report.domain.QReport.report;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dnd.myOcean.report.domain.Report;
import dnd.myOcean.report.dto.response.ReportResponse;
import dnd.myOcean.report.repository.infra.querydsl.dto.ReportReadCondition;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class ReportQuerydslRepositoryImpl extends QuerydslRepositorySupport implements ReportQuerydslRepository {

    private final JPAQueryFactory query;

    public ReportQuerydslRepositoryImpl(JPAQueryFactory query) {
        super(Report.class);
        this.query = query;
    }

    @Override
    public Page<ReportResponse> findAllReport(ReportReadCondition cond) {
        Pageable pageable = PageRequest.of(cond.getPage(), cond.getSize());
        Predicate predicate = createPredicate(cond);
        return new PageImpl<>(fetchAllReport(predicate, pageable), pageable, fetchCount(predicate));
    }

    private Predicate createPredicate(ReportReadCondition cond) {
        BooleanBuilder builder = new BooleanBuilder();
        boolean existReporterEmail = String.valueOf(cond.getReporterEmail()).isEmpty();
        boolean existReportedEmail = String.valueOf(cond.getReportedEmail()).isEmpty();

        if (existReporterEmail) {
            builder.and(report.reporter.email.eq(cond.getReporterEmail()));
        }

        if (existReportedEmail) {
            builder.and(report.reported.email.eq(cond.getReportedEmail()));
        }

        return builder;
    }

    private List<ReportResponse> fetchAllReport(Predicate predicate, Pageable pageable) {
        return getQuerydsl().applyPagination(
                pageable,
                query.select(constructor(ReportResponse.class,
                                report.id,
                                report.reporter.email,
                                report.reported.email,
                                report.reportType,
                                report.content))
                        .from(report)
                        .where(predicate)
                        .orderBy(report.createDate.asc())
        ).fetch();
    }

    private Long fetchCount(Predicate predicate) {
        return query.select(report.count())
                .from(report)
                .where(predicate)
                .fetchOne();
    }
}
