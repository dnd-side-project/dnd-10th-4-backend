package dnd.myOcean.service.oAuth;

import dnd.myOcean.dto.oAuth.response.MemberInfo;

public interface OauthService {
    MemberInfo getMemberInfo(String code) throws Exception;
}
