package dnd.myOcean.service.member;

import dnd.myOcean.dto.oAuth.response.MemberInfo;

public interface MemberService {
    void createMember(MemberInfo memberInfo);
}
