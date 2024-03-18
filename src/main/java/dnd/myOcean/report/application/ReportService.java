package dnd.myOcean.report.application;

import dnd.myOcean.letter.domain.Letter;
import dnd.myOcean.letter.exception.AccessDeniedLetterException;
import dnd.myOcean.letter.repository.infra.jpa.LetterRepository;
import dnd.myOcean.member.domain.Member;
import dnd.myOcean.member.exception.MemberNotFoundException;
import dnd.myOcean.member.repository.infra.jpa.MemberRepository;
import dnd.myOcean.report.domain.Report;
import dnd.myOcean.report.domain.ReportType;
import dnd.myOcean.report.dto.request.ReportSendRequest;
import dnd.myOcean.report.exception.AlreadyReportExistException;
import dnd.myOcean.report.repository.infra.jpa.ReportRepository;
import dnd.myOcean.report.repository.infra.querydsl.dto.PagedReportResponse;
import dnd.myOcean.report.repository.infra.querydsl.dto.ReportReadCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final LetterRepository letterRepository;

    @Transactional
    public void report(final ReportSendRequest request) {
        Member reporter = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        Letter letter = letterRepository.findByIdAndReceiverId(request.getLetterId(), request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);

        if (alreadyReported(request.getMemberId(), letter)) {
            throw new AlreadyReportExistException();
        }

        reportRepository.save(Report.builder()
                .reporter(reporter)
                .reported(letter.getSender())
                .letter(letter)
                .reportType(ReportType.valueOf(request.getReportType()))
                .content(request.getReportContent())
                .build());
    }

    public PagedReportResponse findAllReports(ReportReadCondition cond) {
        return PagedReportResponse.of(reportRepository.findAllReport(cond));
    }
    
    private boolean alreadyReported(final Long reporterId, final Letter letter) {
        return reportRepository.existsByLetterIdAndReporterId(letter.getId(), reporterId);
    }
}
