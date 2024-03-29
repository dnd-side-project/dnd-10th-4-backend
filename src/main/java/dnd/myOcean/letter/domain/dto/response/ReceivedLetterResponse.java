package dnd.myOcean.letter.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dnd.myOcean.letter.domain.Letter;
import dnd.myOcean.letter.domain.LetterTag;
import dnd.myOcean.member.domain.WorryType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReceivedLetterResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    private Long letterId;
    private String letterType;
    private LetterTag letterTag;
    private String senderNickname;
    private String receiverNickname;
    private String content;
    private WorryType worryType;
    private String sendImagePath;

    public static ReceivedLetterResponse toDto(Letter letter) {
        return ReceivedLetterResponse.builder()
                .createdAt(letter.getCreateDate())
                .letterId(letter.getId())
                .letterType(letter.getLetterType())
                .letterTag(letter.getLetterTag())
                .receiverNickname(letter.getReceiver().getNickName())
                .senderNickname(letter.getSender().getNickName())
                .content(letter.getContent())
                .worryType(letter.getWorryType())
                .sendImagePath(letter.getSendletterImage() == null ? null : letter.getSendletterImage().getImagePath())
                .build();
    }
}
