package dnd.myOcean.dto.sign;


import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
