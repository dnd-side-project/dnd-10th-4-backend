package dnd.myOcean.letter.domain;


import dnd.myOcean.global.common.base.BaseEntity;
import dnd.myOcean.letterimage.domain.LetterImage;
import dnd.myOcean.member.domain.Member;
import dnd.myOcean.member.domain.WorryType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
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

    private String letterType;

    @Embedded
    private LetterTag letterTag;

    @Column(updatable = false, length = 1000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member receiver;

    @Enumerated(EnumType.STRING)
    private WorryType worryType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "send_letter_image_id")
    private LetterImage sendletterImage;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "reply_letter_image_id")
    private LetterImage replyletterImage;

    private String uuid;

    private boolean isDeleteBySender;

    private boolean hasReplied;
    private LocalDateTime repliedDate;
    private String replyContent;

    private boolean isStored;

    public static Letter createLetter(final Member sender, final String content, final Member receiver,
                                      final WorryType worryType,
                                      final LetterTag letterTag, final LetterImage sendletterImage, final String uuid) {
        return Letter.builder()
                .sender(sender)
                .letterType("Normal")
                .content(content)
                .receiver(receiver)
                .worryType(worryType)
                .letterTag(letterTag)
                .sendletterImage(sendletterImage)
                .uuid(uuid)
                .isDeleteBySender(false)
                .hasReplied(false)
                .isStored(false)
                .build();
    }

    public static Letter createOnboardingLetter(final Member sender, final Member receiver,
                                                final LetterImage sendletterImage,
                                                final String content) {
        return Letter.builder()
                .sender(sender)
                .letterType("Onboarding")
                .content(content)
                .receiver(receiver)
                .sendletterImage(sendletterImage)
                .isDeleteBySender(false)
                .hasReplied(false)
                .isStored(false)
                .build();
    }

    public static Letter createSpecialLetter(final Member sender, final Member receiver,
                                             final LetterImage sendletterImage,
                                             final String content) {
        return Letter.builder()
                .sender(sender)
                .letterType("Special")
                .content(content)
                .receiver(receiver)
                .sendletterImage(sendletterImage)
                .isDeleteBySender(false)
                .hasReplied(false)
                .isStored(false)
                .build();
    }

    public void deleteBySender() {
        this.isDeleteBySender = true;
    }

    public void reply(final String replyContent, final LetterImage replyletterImage) {
        this.replyContent = replyContent;
        this.hasReplied = true;
        this.replyletterImage = replyletterImage;
        this.repliedDate = LocalDateTime.now();
    }

    public void store(final boolean isStored) {
        this.isStored = isStored;
    }

    public void updateReceiver(final Member receiver) {
        this.receiver = receiver;
    }

    public boolean isNormalLetter() {
        return this.letterType.equals("Normal");
    }
}
