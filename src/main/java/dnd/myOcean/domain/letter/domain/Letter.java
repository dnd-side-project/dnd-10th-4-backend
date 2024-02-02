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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Builder
@Getter
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

    private boolean isDeleteBySender;
    private boolean hasReplied;
    private boolean isStored;
    private String replyContent;
    private String uuid;

    public static Letter createLetter(Member sender, String content, Member receiver, WorryType worryType,
                                      String uuid) {
        return Letter.builder()
                .sender(sender)
                .content(content)
                .receiver(receiver)
                .worryType(worryType)
                .uuid(uuid)
                .isDeleteBySender(false)
                .hasReplied(false)
                .isStored(false)
                .build();
    }

    public void deleteBySender() {
        this.isDeleteBySender = true;
    }

    public void reply(String replyContent) {
        this.replyContent = replyContent;
        this.hasReplied = true;
    }

    public void store(boolean isStored) {
        this.isStored = isStored;
    }

    public void updateReceiver(Member receiver) {
        this.receiver = receiver;
    }
}
