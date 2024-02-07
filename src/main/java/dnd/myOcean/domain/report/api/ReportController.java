package dnd.myOcean.domain.report.api;

import dnd.myOcean.domain.report.application.ReportService;
import dnd.myOcean.domain.report.dto.request.ReportSendRequest;
import dnd.myOcean.global.auth.aop.AssignCurrentMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/{letterId}")
    @AssignCurrentMemberId
    public ResponseEntity<Void> report(@RequestBody ReportSendRequest reportSendRequest,
                                       @PathVariable("letterId") Long letterId) {
        reportService.report(reportSendRequest, letterId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
