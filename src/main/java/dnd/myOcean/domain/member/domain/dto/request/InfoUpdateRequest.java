package dnd.myOcean.domain.member.domain.dto.request;

import dnd.myOcean.domain.member.domain.WorryType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InfoUpdateRequest {

    @Null
    private Long memberId;

    @NotEmpty
    private String nickName;

    @NotEmpty
    private String birthday;

    @NotEmpty
    private String gender;

    @NotEmpty
    private String nickname;

    private List<String> worries;

    public List<WorryType> getWorries() {
        List<WorryType> worryTypes = new ArrayList<>();
        for (String worry : worries) {
            WorryType findWorry = WorryType.from(worry);
            worryTypes.add(findWorry);
        }
        return worryTypes;
    }
}
