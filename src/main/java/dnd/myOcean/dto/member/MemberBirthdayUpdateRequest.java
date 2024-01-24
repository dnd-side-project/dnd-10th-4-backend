package dnd.myOcean.dto.member;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class MemberBirthdayUpdateRequest {

    @Null
    private String email;

    @NotEmpty
    private String birthday;
}
