package dnd.myOcean.config.oAuth.handler;

import dnd.myOcean.config.oAuth.kakao.details.KakaoUserInfo;
import dnd.myOcean.config.security.jwt.token.TokenService;
import dnd.myOcean.domain.member.Member;
import dnd.myOcean.dto.jwt.response.TokenDto;
import dnd.myOcean.exception.member.MemberNotFoundException;
import dnd.myOcean.repository.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String REDIRECT_URI = "http://localhost:8080/api/sign/login/kakao?accessToken=%s&refreshToken=%s";

    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());

        Member member = memberRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        TokenDto tokenDto = tokenService.createToken(member.getEmail(), member.getRole().name());
        String redirectURI = String.format(REDIRECT_URI, tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        getRedirectStrategy().sendRedirect(request, response, redirectURI);
    }
}
