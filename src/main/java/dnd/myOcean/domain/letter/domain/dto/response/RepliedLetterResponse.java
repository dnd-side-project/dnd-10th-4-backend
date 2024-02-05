package dnd.myOcean.domain.letter.domain.dto.response;

import dnd.myOcean.domain.letter.domain.Letter;
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
public class RepliedLetterResponse {

    private LocalDateTime createdAt;
    private Long letterId;
    private String senderNickname;
    private String receiverNickname;
    private String content;
    private String repliedContent;
    private WorryType worryType;

    public static RepliedLetterResponse toDto(Letter letter) {
        return RepliedLetterResponse.builder()
                .createdAt(letter.getCreateDate())
                .letterId(letter.getId())
                .senderNickname(letter.getSender().getNickName())
                .receiverNickname(letter.getReceiver().getNickName())
                .content(letter.getContent())
                .repliedContent(letter.getReplyContent())
                .worryType(letter.getWorryType())
                .build();
    }
}
