package dnd.myOcean.domain.member.dto.response;


import dnd.myOcean.domain.member.domain.WorryType;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoResponse {

    private Long id;
    private String email;
    private String nickname;
    private List<WorryType> worryTypes;
    private String gender;
    private Integer age;
    private String role;
}
