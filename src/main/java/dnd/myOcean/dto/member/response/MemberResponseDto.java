package dnd.myOcean.dto.member.response;

import dnd.myOcean.domain.member.Gender;
import dnd.myOcean.domain.member.Member;
import dnd.myOcean.domain.member.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberResponseDto {

    private Long id;
    private String email;
    private String nickname;
    private String birthday;
    private Gender gender;
    private Role role;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickName();
        this.birthday = member.getBirthday();
        this.gender = member.getGender();
        this.role = member.getRole();
    }
}
