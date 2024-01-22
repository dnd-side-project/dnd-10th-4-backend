package dnd.myOcean.dto.member;


import dnd.myOcean.domain.member.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberGenderUpdateRequest {

    private final Gender gender;
}
