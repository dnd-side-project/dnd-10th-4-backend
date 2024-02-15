package dnd.myOcean.domain.member.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnd.myOcean.domain.member.domain.Gender;
import dnd.myOcean.domain.member.domain.Member;
import dnd.myOcean.domain.member.domain.Role;
import dnd.myOcean.domain.member.domain.dto.request.KakaoLoginRequest;
import dnd.myOcean.domain.member.repository.infra.jpa.MemberRepository;
import dnd.myOcean.global.auth.exception.auth.InvalidAuthCodeException;
import dnd.myOcean.global.auth.exception.auth.ReissueFailException;
import dnd.myOcean.global.auth.jwt.token.TokenProvider;
import dnd.myOcean.global.auth.jwt.token.TokenResponse;
import dnd.myOcean.global.auth.jwt.token.repository.redis.RefreshTokenRedisRepository;
import dnd.myOcean.global.common.auth.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String REFRESH_HEADER = "RefreshToken";
    private static final String PREFIX = "낯선 ";

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-url}")
    private String redirect_uri;

    public TokenResponse kakaoLogin(String code) throws JsonProcessingException {
        /**
         * 1. code로 사용자 정보 받기 (원래는 받은 code를 가지고 토큰 발급 -> 토큰으로 사용자 정보 요청 Flow인데,
         *    포스트맨 내부적으로 code로 token 요청하고, token으로 사용자정보를 받아오는 거 같음.
         * // 포스트맨이 아닌 실제 배포 시에는 getKakaoUserInfo(code) -> getKakaoUserInfo(getToken(code)) 으로 변경해주어야 할 듯.
         */
        KakaoLoginRequest request = getKakaoUserInfo(getToken(code));

        /**
         * 2. 받아온 사용자 정보가 데이터베이스에 없다면 가입 후 리턴, 있으면 리턴
         */
        Member member = saveIfNonExist(request);

        /**
         * 3. JWT 생성
         */
        TokenResponse tokenResponse = tokenProvider.createToken(String.valueOf(member.getId()),
                member.getEmail(),
                member.getRole().name());

        /**
         * 4. Redis에 RefreshToken 저장
         */
        saveRefreshTokenOnRedis(member, tokenResponse);

        /**
         * 5. token 리턴
         */
        return tokenResponse;
    }

    private void saveRefreshTokenOnRedis(Member member, TokenResponse response) {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(member.getRole().name()));
        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(member.getId())
                .email(member.getEmail())
                .authorities(simpleGrantedAuthorities)
                .refreshToken(response.getRefreshToken())
                .build());
    }

    private Member saveIfNonExist(KakaoLoginRequest request) {
        return memberRepository.findByEmail(request.getEmail())
                .orElseGet(() ->
                        memberRepository.save(
                                Member.builder()
                                        .email(request.getEmail())
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
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
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

    private KakaoLoginRequest getKakaoUserInfo(String token) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();

        // HTTP 헤더 생성
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
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
            return new KakaoLoginRequest(email);
        } catch (HttpClientErrorException e) {
            throw new InvalidAuthCodeException();
        }
    }

    public TokenResponse reissueAccessToken(HttpServletRequest request) {
        String refreshToken = getTokenFromHeader(request, REFRESH_HEADER);

        if (!tokenProvider.validate(refreshToken) || !tokenProvider.validateExpire(refreshToken)) {
            throw new ReissueFailException();
        }

        RefreshToken findToken = refreshTokenRedisRepository.findByRefreshToken(refreshToken);

        TokenResponse tokenResponse = tokenProvider.createToken(
                String.valueOf(findToken.getId()),
                findToken.getEmail(),
                findToken.getAuthority());

        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(findToken.getId())
                .email(findToken.getEmail())
                .authorities(findToken.getAuthorities())
                .refreshToken(tokenResponse.getRefreshToken())
                .build());

        SecurityContextHolder.getContext()
                .setAuthentication(tokenProvider.getAuthentication(tokenResponse.getAccessToken()));

        return tokenResponse;
    }

    private String getTokenFromHeader(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        if (StringUtils.hasText(token)) {
            return token;
        }
        return null;
    }
}
