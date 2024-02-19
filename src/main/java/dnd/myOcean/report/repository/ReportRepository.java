package dnd.myOcean.report.repository;

import dnd.myOcean.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByLetterIdAndReporterId(Long id, Long memberId);
}
