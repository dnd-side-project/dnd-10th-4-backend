package dnd.myOcean.config.oAuth.kakao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class KakaoUserInfo {

    public static final String KAKAO_ACCOUNT = "kakao_account";
    private static final String PROFILE = "profile";
    public static final String EMAIL = "email";
    private static final String NICKNAME = "nickname";

    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getEmail() {
        ObjectMapper objectMapper = new ObjectMapper();
        Object kakaoAccount = attributes.get(KAKAO_ACCOUNT);

        Map<String, Object> account = objectMapper.convertValue(kakaoAccount,
                new TypeReference<Map<String, Object>>() {
                });

        return (String) account.get(EMAIL);
    }

    public String getName() {
        ObjectMapper objectMapper = new ObjectMapper();
        Object kakaoAccount = attributes.get(KAKAO_ACCOUNT);

        Map<String, Object> account = objectMapper.convertValue(kakaoAccount, new TypeReference<>() {
        });

        Object profile = account.get(PROFILE);
        Map<String, Object> profileMap = objectMapper.convertValue(profile, new TypeReference<>() {
        });

        return (String) profileMap.get(NICKNAME);
    }
}
