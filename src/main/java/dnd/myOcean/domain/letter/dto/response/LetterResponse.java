package dnd.myOcean.domain.letter.dto.response;

import dnd.myOcean.domain.letter.domain.Letter;
import dnd.myOcean.domain.member.domain.WorryType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterResponse {

    private String senderNickname;
    private String receiverNickname;
    private String content;
    private WorryType worryType;
    private boolean isRead;

    public static LetterResponse toDto(Letter letter) {
        return LetterResponse.builder()
                .senderNickname(letter.getSender().getNickName())
                .receiverNickname(letter.getReceiver().getNickName())
                .content(letter.getContent())
                .worryType(letter.getWorryType())
                .isRead(letter.isRead())
                .build();
    }
}
