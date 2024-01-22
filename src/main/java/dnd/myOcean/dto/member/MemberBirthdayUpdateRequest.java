package dnd.myOcean.dto.member;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberBirthdayUpdateRequest {

    private final String birthday;
}
