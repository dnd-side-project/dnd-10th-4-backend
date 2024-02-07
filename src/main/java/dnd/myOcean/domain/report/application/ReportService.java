package dnd.myOcean.domain.report.application;

import dnd.myOcean.domain.letter.domain.Letter;
import dnd.myOcean.domain.letter.exception.AccessDeniedLetterException;
import dnd.myOcean.domain.letter.repository.infra.jpa.LetterRepository;
import dnd.myOcean.domain.member.domain.Member;
import dnd.myOcean.domain.member.exception.MemberNotFoundException;
import dnd.myOcean.domain.member.repository.infra.jpa.MemberRepository;
import dnd.myOcean.domain.report.domain.Report;
import dnd.myOcean.domain.report.dto.request.ReportSendRequest;
import dnd.myOcean.domain.report.exception.AlreadyReportExistException;
import dnd.myOcean.domain.report.repository.ReportRepository;
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
    public void report(ReportSendRequest request) {
        Member reporter = memberRepository.findById(request.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        Letter letter = letterRepository.findByIdAndReceiverId(request.getLetterId(), request.getMemberId())
                .orElseThrow(AccessDeniedLetterException::new);

        if (alreadyReported(request, letter)) {
            throw new AlreadyReportExistException();
        }

        reportRepository.save(Report.builder()
                .reporter(reporter)
                .reported(letter.getSender())
                .letter(letter)
                .content(request.getReportContent())
                .build());
    }

    private boolean alreadyReported(ReportSendRequest request, Letter letter) {
        return reportRepository.existsByLetterIdAndReporterId(letter.getId(), request.getMemberId());
    }
}
