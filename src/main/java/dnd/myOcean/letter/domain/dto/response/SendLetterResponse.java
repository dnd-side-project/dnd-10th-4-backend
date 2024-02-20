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
public class SendLetterResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private Long letterId;
    private LetterTag letterTag;
    private String senderNickname;
    private String content;
    private WorryType worryType;
    private String sendImagePath;

    public static SendLetterResponse toDto(Letter letter) {
        return SendLetterResponse.builder()
                .createdAt(letter.getCreateDate())
                .letterTag(letter.getLetterTag())
                .letterId(letter.getId())
                .senderNickname(letter.getSender().getNickName())
                .content(letter.getContent())
                .worryType(letter.getWorryType())
                .sendImagePath(letter.getSendletterImage() == null ? null : letter.getSendletterImage().getImagePath())
                .build();
    }
}
