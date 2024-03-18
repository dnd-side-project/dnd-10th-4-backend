package dnd.myOcean.report.repository.infra.querydsl;

import dnd.myOcean.report.dto.response.ReportResponse;
import dnd.myOcean.report.repository.infra.querydsl.dto.ReportReadCondition;
import org.springframework.data.domain.Page;

public interface ReportQuerydslRepository {

    Page<ReportResponse> findAllReport(ReportReadCondition cond);
}
