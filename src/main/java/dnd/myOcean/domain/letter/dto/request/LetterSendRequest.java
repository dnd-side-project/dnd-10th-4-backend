package dnd.myOcean.domain.letter.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterSendRequest {

    @Null
    private Long memberId;

    @NotEmpty
    private String content;

    private boolean equalGender;
    private Integer ageRangeStart;
    private Integer ageRangeEnd;

    @NotEmpty
    private String worryType;
}
