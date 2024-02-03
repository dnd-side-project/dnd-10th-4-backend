package dnd.myOcean.domain.letter.dto.response;

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
public class ReceivedLetterResponse {

    private LocalDateTime createdAt;
    private Long letterId;
    private String senderNickname;
    private String receiverNickname;
    private String content;
    private WorryType worryType;

    public static ReceivedLetterResponse toDto(Letter letter) {
        return ReceivedLetterResponse.builder()
                .createdAt(letter.getCreateDate())
                .letterId(letter.getId())
                .receiverNickname(letter.getReceiver().getNickName())
                .senderNickname(letter.getSender().getNickName())
                .content(letter.getContent())
                .worryType(letter.getWorryType())
                .build();
    }
}
