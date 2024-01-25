package dnd.myOcean.service.sign;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnd.myOcean.config.security.jwt.token.TokenService;
import dnd.myOcean.domain.member.Gender;
import dnd.myOcean.domain.member.Member;
import dnd.myOcean.domain.member.Role;
import dnd.myOcean.dto.jwt.response.TokenDto;
import dnd.myOcean.dto.sign.LoginKakaoRequestDto;
import dnd.myOcean.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
public class SignService {

    private static final String PREFIX = "낯선 ";

    private final RestTemplate restTemplate;
    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-url}")
    private String redirect_uri;

    public TokenDto kakaoLogin(String code) throws JsonProcessingException {
        /**
         * 1. code로 사용자 정보 받기 ( code -> 토큰 발급 -> 토큰으로 사용자 정보 요청 Flow인데, 포스트맨 내부적으로 token 요청을 하는 것 같음)
         * // 포스트맨이 아닌 실제 배포 시에는 getKakaoUserInfo(code) -> getKakaoUserInfo(getToken(code)) 으로 변경해주어야 할 듯함.
         */
        LoginKakaoRequestDto loginKakaoRequestDto = getKakaoUserInfo(code);

        /**
         * 2. 받은 사용자 정보가 데이터베이스에 없다면 가입 후 리턴, 있으면 리턴
         */
        Member member = saveIfNonExist(loginKakaoRequestDto);

        /**
         * 3. JWT 생성 및 리턴
         */
        return tokenService.createToken(member.getEmail(), member.getRole().name());
    }

    private Member saveIfNonExist(LoginKakaoRequestDto loginKakaoRequestDto) {
        return memberRepository.findByEmail(loginKakaoRequestDto.getEmail())
                .orElseGet(() ->
                        memberRepository.save(
                                Member.builder()
                                        .email(loginKakaoRequestDto.getEmail())
                                        .role(Role.USER)
                                        .nickName(PREFIX)
                                        .gender(Gender.NONE)
                                        .updateAgeCount(0)
                                        .updateGenderCount(0)
                                        .build()
                        )
                );
    }

    private String getToken(String code) throws JsonProcessingException {
        // HTTP 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 바디 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", redirect_uri);
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private LoginKakaoRequestDto getKakaoUserInfo(String token) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();

        // HTTP 헤더 생성
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String email = jsonNode.get("kakao_account").get("email").asText();

        return new LoginKakaoRequestDto(email);
    }
}
