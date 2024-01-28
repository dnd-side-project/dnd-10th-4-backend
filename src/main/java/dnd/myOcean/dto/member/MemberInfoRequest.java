package dnd.myOcean.dto.member;

import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoRequest {

    @Null
    private String email;
}
