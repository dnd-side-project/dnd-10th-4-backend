package dnd.myOcean.dto.member;

import dnd.myOcean.domain.member.Worry;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MemberWorryUpdateRequest {

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
