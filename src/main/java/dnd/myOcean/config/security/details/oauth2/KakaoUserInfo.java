package dnd.myOcean.config.security.details.oauth2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class KakaoUserInfo {

    public static final String KAKAO = "kakao";
    public static final String KAKAO_ACCOUNT = "kakao_account";
    public static final String EMAIL = "email";

    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        System.out.println(attributes.get("email"));
        this.attributes = attributes;
    }

    public String getEmail() {
        ObjectMapper objectMapper = new ObjectMapper();
        Object kakaoAccount = attributes.get(KAKAO_ACCOUNT);
        Map<String, Object> account = objectMapper.convertValue(kakaoAccount, new TypeReference<Map<String, Object>>() {
        });

        return (String) account.get(EMAIL);
    }
}
