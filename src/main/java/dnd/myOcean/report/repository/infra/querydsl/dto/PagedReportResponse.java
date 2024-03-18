package dnd.myOcean.report.repository.infra.querydsl.dto;

import dnd.myOcean.report.dto.response.ReportResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;


@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PagedReportResponse {

    private Long totalElements;
    private Integer totalPage;
    private boolean hasNextPage;
    private List<ReportResponse> letters;

    public static PagedReportResponse of(Page<ReportResponse> page) {
        return new PagedReportResponse(page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.getContent());
    }
}
