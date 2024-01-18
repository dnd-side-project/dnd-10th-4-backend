package dnd.myOcean.config.security.details.oauth2;

import java.util.Map;

public class KakaoUserInfo {

    public static final String KAKAO = "kakao";
    public static final String KAKAO_ACCOUNT = "kakao_account";
    public static final String EMAIL = "email";

    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getEmail() {
        return (String) attributes.get(EMAIL);
    }
}
