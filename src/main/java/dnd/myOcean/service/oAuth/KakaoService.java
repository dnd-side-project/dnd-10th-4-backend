package dnd.myOcean.service.oAuth;

import dnd.myOcean.config.oAuth.kakao.KakaoConfig;
import dnd.myOcean.dto.oAuth.response.MemberInfo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoConfig kakaoConfig;
    private final RestTemplate restTemplate;

    public MemberInfo getMemberInfo(String code) throws Exception {
        if (code == null) {
            throw new Exception("Failed get authorization code");
        }

        String accessToken = "";
        String refreshToken = "";

        try {
            HttpHeaders headers = createHeaders(Optional.empty());

            MultiValueMap<String, String> params = createParams(code);

            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    kakaoConfig.getKakaoAuthUrlK(),
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            JSONObject jsonObj = parseJson(response.getBody());

            accessToken = (String) jsonObj.get("access_token");
            refreshToken = (String) jsonObj.get("refresh_token");

        } catch (Exception e) {
            throw new Exception("API call failed");
        }
        return getMemberInfoWithToken(accessToken);
    }

    private MemberInfo getMemberInfoWithToken(String accessToken) throws Exception {
        //HttpHeader 생성
        HttpHeaders headers = createHeaders(Optional.ofNullable(accessToken));

        //HttpHeader 담기
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                kakaoConfig.getKakaoApiUrl(),
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        //Response 데이터 파싱
        JSONObject jsonObj = parseJson(response.getBody());

        JSONObject account = (JSONObject) jsonObj.get("kakao_account");
        JSONObject profile = (JSONObject) account.get("profile");

        long id = (long) jsonObj.get("id");
        String email = String.valueOf(account.get("email"));
        String nickname = String.valueOf(profile.get("nickname"));

        return MemberInfo.builder()
                .id(id)
                .email(email)
                .nickName(nickname)
                .build();
    }

    private HttpHeaders createHeaders(Optional<String> accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        if (accessToken.isPresent()) {
            headers.add("Authorization", "Bearer " + accessToken.get());
        }
        return headers;
    }

    private MultiValueMap<String, String> createParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoConfig.getKakaoClientId());
        params.add("client_secret", kakaoConfig.getKakaoClientSecret());
        params.add("code", code);
        params.add("redirect_uri", kakaoConfig.getKakaoRedirectUri());
        return params;
    }

    private JSONObject parseJson(String body) throws Exception {
        JSONParser jsonParser = new JSONParser();
        return (JSONObject) jsonParser.parse(body);
    }
}
