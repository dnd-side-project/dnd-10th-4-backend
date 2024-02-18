package dnd.myOcean.domain.letter.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dnd.myOcean.domain.letter.domain.Letter;
import dnd.myOcean.domain.letter.domain.LetterTag;
import dnd.myOcean.domain.member.domain.WorryType;
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
    private String imagePath;

    // 보관한 편지 단건 조회 API 추가 시 사용 예정
    public static StoredLetterResponse toDto(Letter letter) {
        return StoredLetterResponse.builder()
                .createdAt(letter.getCreateDate())
                .letterId(letter.getId())
                .letterTag(letter.getLetterTag())
                .senderNickname(letter.getSender().getNickName())
                .receiverNickname(letter.getReceiver().getNickName())
                .content(letter.getContent())
                .repliedContent(letter.getReplyContent())
                .worryType(letter.getWorryType())
                .imagePath(letter.getLetterImage() == null ? "이미지가 존재하지 않습니다" : letter.getLetterImage().getImagePath())
                .build();
    }
}
