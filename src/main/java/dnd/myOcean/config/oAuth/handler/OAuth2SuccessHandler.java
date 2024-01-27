package dnd.myOcean.config.oAuth.handler;

import dnd.myOcean.config.oAuth.kakao.details.KakaoUserInfo;
import dnd.myOcean.config.security.jwt.token.TokenProvider;
import dnd.myOcean.domain.member.Member;
import dnd.myOcean.domain.refreshtoken.RefreshToken;
import dnd.myOcean.dto.jwt.response.TokenDto;
import dnd.myOcean.exception.member.MemberNotFoundException;
import dnd.myOcean.repository.jpa.member.MemberRepository;
import dnd.myOcean.repository.redis.RefreshTokenRedisRepository;
import dnd.myOcean.util.HttpRequestUtil;
import jakarta.servlet.ServletException;
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
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String REDIRECT_URI = "http://localhost:8080/api/sign/login/kakao?accessToken=%s&refreshToken=%s";

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());

        Member member = memberRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        TokenDto tokenDto = tokenProvider.createToken(member.getEmail(), member.getRole().name());

        saveRefreshTokenOnRedis(request, member, tokenDto);
        String redirectURI = String.format(REDIRECT_URI, tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        getRedirectStrategy().sendRedirect(request, response, redirectURI);
    }

    private void saveRefreshTokenOnRedis(HttpServletRequest request, Member member, TokenDto tokenDto) {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority(member.getRole().name()));
        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(member.getEmail())
                .ip(HttpRequestUtil.getClientIp(request))
                .authorities(simpleGrantedAuthorities)
                .refreshToken(tokenDto.getRefreshToken())
                .build());
    }
}
