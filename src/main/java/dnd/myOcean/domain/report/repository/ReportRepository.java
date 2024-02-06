package dnd.myOcean.domain.report.repository;

import dnd.myOcean.domain.member.domain.Member;
import dnd.myOcean.domain.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("select r from Report r where r.reporter.id = :reporterId")
    Optional<Report> findByReporterId(@Param("reporterId") Long reporterId);

    @Query("select r from Report r where r.reported.id = :reportedId")
    Optional<Report> findByReportedId(@Param("reportedId") Long reportedId);

    Optional<Report> findByReporterAndReported(Member reporter,
                                     Member reported);
}
