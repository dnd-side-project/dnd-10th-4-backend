package dnd.myOcean.domain.letter.domain;


import dnd.myOcean.domain.member.domain.Member;
import dnd.myOcean.domain.member.domain.WorryType;
import dnd.myOcean.global.common.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Letter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "letter_id")
    private Long id;

    @Column(updatable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @Enumerated(EnumType.STRING)
    private WorryType worryType;
    
    private boolean isRead;

    private boolean isDeleteBySender;

    private boolean isDeleteByReceiver;

    private boolean hasReplied;
    private String replyContent;

    public void read() {
        this.isRead = true;
    }

    public void deleteBySender() {
        this.isDeleteBySender = true;
    }

    public void deleteByReceiver() {
        this.isDeleteByReceiver = true;
    }

    public void replied(String replyContent) {
        this.replyContent = replyContent;
        this.hasReplied = true;
    }

    public static Letter createLetter(Member sender, String content, Member receiver, WorryType worryType) {
        return Letter.builder()
                .sender(sender)
                .content(content)
                .receiver(receiver)
                .worryType(worryType)
                .isRead(false)
                .isDeleteBySender(false)
                .isDeleteByReceiver(false)
                .hasReplied(false)
                .build();
    }
}
