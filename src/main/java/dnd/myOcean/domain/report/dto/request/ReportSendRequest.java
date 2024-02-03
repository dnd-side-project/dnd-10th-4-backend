package dnd.myOcean.domain.report.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportSendRequest {

    @Null
    private Long memberId;

    @NotBlank(message = "신고 내용을 입력해주세요.")
    private String reportContent;
}
