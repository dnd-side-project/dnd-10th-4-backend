package dnd.myOcean.report.api;

import dnd.myOcean.report.application.ReportService;
import dnd.myOcean.report.dto.request.ReportSendRequest;
import dnd.myOcean.global.auth.aop.AssignCurrentMemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    @AssignCurrentMemberId
    public ResponseEntity<Void> report(@RequestBody ReportSendRequest reportSendRequest) {
        reportService.report(reportSendRequest);
        return new ResponseEntity(HttpStatus.OK);
    }
}