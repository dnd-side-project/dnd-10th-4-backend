package dnd.myOcean.dto.member.response;


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
    private String gender;
    private Integer age;
    private String role;
}
