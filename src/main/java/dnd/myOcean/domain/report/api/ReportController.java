package dnd.myOcean.domain.report.api;

import dnd.myOcean.domain.report.application.ReportService;
import dnd.myOcean.domain.report.dto.request.ReportSendRequest;
import dnd.myOcean.global.auth.aop.AssignCurrentMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    @PatchMapping("{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity report(@RequestBody ReportSendRequest reportSendRequest,
                                 @PathVariable("letterId") Long letterId) {
        reportService.report(reportSendRequest, letterId);

        return new ResponseEntity(HttpStatus.OK);
    }
}
