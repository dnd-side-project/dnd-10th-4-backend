package dnd.myOcean.global.auth.oauth.kakao.details;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class KakaoUserInfo {

    public static final String KAKAO_ACCOUNT = "kakao_account";
    public static final String EMAIL = "email";

    private Map<String, Object> attributes;

    public KakaoUserInfo(final Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getEmail() {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<Map<String, Object>> typeReferencer = new TypeReference<>() {
        };

        Object kakaoAccount = attributes.get(KAKAO_ACCOUNT);
        Map<String, Object> account = objectMapper.convertValue(kakaoAccount, typeReferencer);

        return (String) account.get(EMAIL);
    }
}
