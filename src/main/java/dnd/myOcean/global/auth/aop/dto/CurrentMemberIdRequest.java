package dnd.myOcean.global.auth.aop.dto;


import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CurrentMemberIdRequest {

    @Null
    private Long memberId;
}
