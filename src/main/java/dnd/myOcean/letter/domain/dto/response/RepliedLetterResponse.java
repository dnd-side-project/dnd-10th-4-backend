package dnd.myOcean.letter.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dnd.myOcean.letter.domain.Letter;
import dnd.myOcean.letter.domain.LetterTag;
import dnd.myOcean.member.domain.WorryType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RepliedLetterResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime repliedAt;

    private Long letterId;
    private LetterTag letterTag;
    private String senderNickname;
    private String receiverNickname;
    private String content;
    private String repliedContent;
    private WorryType worryType;
    private String imagePath;

    public static RepliedLetterResponse toDto(Letter letter) {
        return RepliedLetterResponse.builder()
                .createdAt(letter.getCreateDate())
                .repliedAt(letter.getRepliedDate())
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

    public static List<RepliedLetterResponse> toDtoList(List<Letter> letters) {
        return letters.stream().map(letter -> toDto(letter))
                .collect(Collectors.toList());
    }
}
