package dnd.myOcean.report.repository.infra.jpa;

import dnd.myOcean.report.domain.Report;
import dnd.myOcean.report.repository.infra.querydsl.ReportQuerydslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportQuerydslRepository {

    boolean existsByLetterIdAndReporterId(Long id, Long memberId);
}
