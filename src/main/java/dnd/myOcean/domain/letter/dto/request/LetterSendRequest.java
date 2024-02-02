package dnd.myOcean.domain.letter.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterSendRequest {

    @Null
    private Long memberId;

    @NotEmpty(message = "편지 내용을 입력해주세요.")
    private String content;

    private boolean equalGender;

    @NotEmpty(message = "수신자의 최소 나이를 지정해주세요.")
    private Integer ageRangeStart;

    @NotEmpty(message = "수신자의 최소 나이를 지정해주세요.")
    private Integer ageRangeEnd;

    @NotEmpty
    private String worryType;
}
