package dnd.myOcean.member.domain.dto.response;


import dnd.myOcean.member.domain.WorryType;
import java.time.LocalDate;
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
    private LocalDate birthDay;
    private Integer age;
    private String role;
}
