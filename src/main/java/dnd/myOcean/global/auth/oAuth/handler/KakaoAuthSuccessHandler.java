package dnd.myOcean.global.auth.oAuth.handler;

import dnd.myOcean.domain.member.domain.Member;
import dnd.myOcean.domain.member.exception.exceptions.MemberNotFoundException;
import dnd.myOcean.domain.member.repository.infra.jpa.MemberRepository;
import dnd.myOcean.global.auth.jwt.token.TokenProvider;
import dnd.myOcean.global.auth.jwt.token.TokenResponse;
import dnd.myOcean.global.auth.jwt.token.repository.redis.RefreshTokenRedisRepository;
import dnd.myOcean.global.auth.oAuth.kakao.details.KakaoUserInfo;
import dnd.myOcean.global.common.auth.RefreshToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KakaoAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String REDIRECT_URI = "http://localhost:8080/api/auth/login/kakao?accessToken=%s&refreshToken=%s";

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());

        Member member = memberRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        TokenResponse tokenResponse = tokenProvider.createToken(String.valueOf(member.getId()),
                member.getEmail(),
                member.getRole().name());

        saveRefreshTokenOnRedis(member, tokenResponse);
        String redirectURI = String.format(REDIRECT_URI, tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken());
        getRedirectStrategy().sendRedirect(request, response, redirectURI);
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
}
