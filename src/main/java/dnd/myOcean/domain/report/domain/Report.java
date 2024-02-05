package dnd.myOcean.domain.report.domain;

import dnd.myOcean.domain.letter.domain.Letter;
import dnd.myOcean.domain.member.domain.Member;
import dnd.myOcean.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Column(updatable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id")
    private Member reported;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "letter_id")
    private Letter letter;

    public Report(Letter letter, Member reporter, Member reported, String reportContent) {
        this.reporter = reporter;
        this.reported = reported;
        this.letter = letter;
        this.content = reportContent;
    }
}
