package dnd.myOcean.letter.domain.dto;


import dnd.myOcean.letter.domain.LetterTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LetterTagDto {

    private Integer ageStart;
    private Integer ageRangeEnd;
    private boolean equalGender;

    public static LetterTagDto toDto(LetterTag letterTag) {
        return new LetterTagDto(letterTag.getAgeRangeStart(),
                letterTag.getAgeRangeEnd(),
                letterTag.isEqualGender());
    }
}
