package dnd.myOcean.dto.oAuth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberInfo {
    private final Long id;
    private final String nickName;
    private final String profileImageUrl;
}
