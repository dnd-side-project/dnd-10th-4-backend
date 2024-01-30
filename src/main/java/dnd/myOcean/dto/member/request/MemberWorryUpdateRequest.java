package dnd.myOcean.dto.member.request;

import dnd.myOcean.domain.member.WorryType;
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
public class MemberWorryUpdateRequest {

    @Null
    private String email;

    private List<String> worries;

    public List<WorryType> getWorries() {
        List<WorryType> store = new ArrayList<>();
        for (String worry : worries) {
            WorryType findWorry = WorryType.from(worry);
            store.add(findWorry);
        }
        return store;
    }
}
