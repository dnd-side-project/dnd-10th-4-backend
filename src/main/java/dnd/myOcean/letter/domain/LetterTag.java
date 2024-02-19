package dnd.myOcean.letter.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterTag {

    private Integer ageStart;
    private Integer ageRangeEnd;
    private boolean equalGender;

    public static LetterTag of(Integer ageRangeStart, Integer ageRangeEnd, boolean equalGender) {
        return new LetterTag(ageRangeStart, ageRangeEnd, equalGender);
    }
}
