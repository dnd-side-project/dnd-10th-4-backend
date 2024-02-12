package dnd.myOcean.domain.letter.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    
    private Long letterId;
    private String senderNickname;
    private String content;
    private WorryType worryType;
    private String imagePath;

    public static SendLetterResponse toDto(Letter letter) {
        return SendLetterResponse.builder()
                .createdAt(letter.getCreateDate())
                .letterId(letter.getId())
                .senderNickname(letter.getSender().getNickName())
                .content(letter.getContent())
                .worryType(letter.getWorryType())
                .imagePath(letter.getLetterImage() == null ? "이미지가 존재하지 않습니다" : letter.getLetterImage().getImagePath())
                .build();
    }
}
