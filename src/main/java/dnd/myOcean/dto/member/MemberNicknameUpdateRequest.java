package dnd.myOcean.dto.member;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberNicknameUpdateRequest {

    @Null
    private String email;

    @NotEmpty
    private String nickname;
}
