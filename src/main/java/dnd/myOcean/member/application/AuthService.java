package dnd.myOcean.member.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnd.myOcean.global.auth.exception.auth.InvalidAuthCodeException;
import dnd.myOcean.global.auth.exception.auth.ReissueFailException;
import dnd.myOcean.global.auth.jwt.token.LoginResponse;
import dnd.myOcean.global.auth.jwt.token.TokenProvider;
import dnd.myOcean.global.auth.jwt.token.TokenResponse;
import dnd.myOcean.global.auth.jwt.token.repository.redis.RefreshTokenRedisRepository;
import dnd.myOcean.global.common.auth.RefreshToken;
import dnd.myOcean.letter.domain.Letter;
import dnd.myOcean.letter.repository.infra.jpa.LetterRepository;
import dnd.myOcean.letterimage.domain.LetterImage;
import dnd.myOcean.letterimage.exception.LetterImageNotFoundException;
import dnd.myOcean.letterimage.repository.infra.jpa.LetterImageRepository;
import dnd.myOcean.member.domain.Member;
import dnd.myOcean.member.domain.dto.request.KakaoLoginRequest;
import dnd.myOcean.member.exception.MemberNotFoundException;
import dnd.myOcean.member.repository.infra.jpa.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private static final String REFRESH_HEADER = "RefreshToken";

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final LetterRepository letterRepository;
    private final LetterImageRepository letterImageRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Value("${letter.onboarding.content}")
    private String onboardingContent;

    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-url}")
    private String redirect_uri;

    @Transactional
    public LoginResponse kakaoLogin(final String code) throws JsonProcessingException {
        String token = getToken(code);
        KakaoLoginRequest request = getKakaoUserInfo(token);

        Member member = saveIfNonExist(request);

        TokenResponse tokenResponse = tokenProvider.createToken(String.valueOf(member.getId()),
                member.getEmail(),
                member.getRole().name());

        saveRefreshTokenOnRedis(member, tokenResponse);

        if (member.isFirstLogin()) {
            Member rootMember = memberRepository.findRootUser().orElseThrow(MemberNotFoundException::new);
            LetterImage letterImage = letterImageRepository.findOnboardingLetter()
                    .orElseThrow(LetterImageNotFoundException::new);
            letterRepository.save(Letter.createOnboardingLetter(rootMember, member, letterImage, onboardingContent));

            return LoginResponse.of(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken(), true);
        }

        return LoginResponse.of(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken(), false);
    }

    @Transactional
    public LoginResponse kakaoLoginForPostman(final String code) throws JsonProcessingException {
        KakaoLoginRequest request = getKakaoUserInfo(code);
        Member member = saveIfNonExist(request);

        TokenResponse tokenResponse = tokenProvider.createToken(String.valueOf(member.getId()),
                member.getEmail(),
                member.getRole().name());

        saveRefreshTokenOnRedis(member, tokenResponse);

        if (member.isFirstLogin()) {
            return LoginResponse.of(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken(), true);
        }

        return LoginResponse.of(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken(), false);
    }

    private void saveRefreshTokenOnRedis(final Member member, final TokenResponse response) {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(member.getRole().name()));
        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(member.getId())
                .email(member.getEmail())
                .authorities(simpleGrantedAuthorities)
                .refreshToken(response.getRefreshToken())
                .build());
    }

    @Transactional
    public Member saveIfNonExist(final KakaoLoginRequest request) {
        return memberRepository.findByEmail(request.getEmail())
                .orElseGet(() ->
                        memberRepository.save(
                                Member.createFirstLoginMember(request.getEmail())
                        )
                );
    }

    private String getToken(final String code) throws JsonProcessingException {
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

    private KakaoLoginRequest getKakaoUserInfo(final String token) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

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

    public TokenResponse reissueAccessToken(final HttpServletRequest request) {
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

    private String getTokenFromHeader(final HttpServletRequest request, final String headerName) {
        String token = request.getHeader(headerName);
        if (StringUtils.hasText(token)) {
            return token;
        }
        return null;
    }
}
