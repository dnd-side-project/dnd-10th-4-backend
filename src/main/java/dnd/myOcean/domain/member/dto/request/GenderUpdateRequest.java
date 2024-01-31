package dnd.myOcean.domain.member.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenderUpdateRequest {

    @Null
    private String email;

    @NotEmpty
    private String gender;
}
