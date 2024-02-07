package dnd.myOcean.domain.report.repository;

import dnd.myOcean.domain.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
