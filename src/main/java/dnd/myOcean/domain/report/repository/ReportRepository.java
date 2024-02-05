package dnd.myOcean.domain.report.repository;

import dnd.myOcean.domain.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByReporterIdAndReportedId(@Param("reporterId") Long reporterId, @Param("reportedId") Long reportedId);
}
