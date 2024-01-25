package dnd.myOcean.dto.member;

import dnd.myOcean.domain.member.Worry;
import jakarta.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberWorryUpdateRequest {

    @Null
    private String email;

    private List<String> worries;

    public List<Worry> getWorries() {
        List<Worry> store = new ArrayList<>();
        for (String worry : worries) {
            Worry findWorry = Worry.from(worry);
            store.add(findWorry);
        }
        return store;
    }
}
