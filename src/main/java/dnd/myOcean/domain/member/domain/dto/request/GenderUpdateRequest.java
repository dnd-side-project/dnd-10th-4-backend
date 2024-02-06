package dnd.myOcean.domain.member.domain.dto.request;


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
    private Long memberId;

    @NotEmpty
    private String gender;
}
