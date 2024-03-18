package dnd.myOcean.report.dto.response;

import dnd.myOcean.report.domain.Report;
import dnd.myOcean.report.domain.ReportType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportResponse {

    private Long id;
    private String reporterEmail;
    private String reportedEmail;
    private ReportType reportType;
    private String content;

    public static ReportResponse of(Report report) {
        return new ReportResponse(report.getId(),
                report.getReporter().getEmail(),
                report.getReported().getEmail(),
                report.getReportType(),
                report.getContent());
    }
}
