package dnd.myOcean.member.domain.dto.response;


import dnd.myOcean.member.domain.Member;
import dnd.myOcean.member.domain.WorryType;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoResponse {

    private Long id;
    private String role;
    private String email;
    private String nickname;
    private String gender;
    private LocalDate birthDay;
    private Integer age;
    private List<WorryType> worryTypes;
    private Integer letterCount;

    public static MemberInfoResponse of(final Member member) {
        List<WorryType> worryTypes = member.getWorries().stream()
                .map(memberWorry -> memberWorry.getWorry().getWorryType())
                .collect(Collectors.toList());

        return new MemberInfoResponse(member.getId(),
                member.getRole().name(),
                member.getEmail(),
                member.getNickName(),
                member.getGender().name(),
                member.getBirthDay(),
                member.getAge(),
                worryTypes,
                member.getLetterCount());
    }
}
