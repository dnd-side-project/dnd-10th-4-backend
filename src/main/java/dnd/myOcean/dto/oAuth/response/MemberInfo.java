package dnd.myOcean.dto.oAuth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberInfo {
    private Long id;
    private String nickName;
    private String profileImageUrl;
}
