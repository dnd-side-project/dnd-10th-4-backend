package dnd.myOcean.letter.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class StoredLetterResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    private Long letterId;
    private LetterTag letterTag;
    private String senderNickname;
    private String receiverNickname;
    private String content;
    private String repliedContent;
    private WorryType worryType;
    private String sendImagePath;
    private String replyImagePath;
}
