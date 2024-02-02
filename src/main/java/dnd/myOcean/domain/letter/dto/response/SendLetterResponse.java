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
public class SendLetterResponse {

    private LocalDateTime createdAt;
    private Long letterId;
    private String senderNickname;
    private String content;
    private WorryType worryType;

    public static SendLetterResponse toDto(Letter letter) {
        return SendLetterResponse.builder()
                .createdAt(letter.getCreateDate())
                .letterId(letter.getId())
                .senderNickname(letter.getSender().getNickName())
                .content(letter.getContent())
                .worryType(letter.getWorryType())
                .build();
    }
}
