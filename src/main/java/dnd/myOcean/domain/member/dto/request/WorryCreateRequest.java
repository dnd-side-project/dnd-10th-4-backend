package dnd.myOcean.domain.member.dto.request;

import dnd.myOcean.domain.member.domain.WorryType;
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
public class WorryCreateRequest {

    @Null
    private Long memberId;
    
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
