package dnd.myOcean.config.oAuth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenService tokenProvider;
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());

        Member member = memberRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        TokenDto tokenDto = tokenProvider.createToken(member.getEmail(), member.getRole().name());
        writeTokenResponse(response, tokenDto);
    }

    private void writeTokenResponse(HttpServletResponse response, TokenDto tokenDto)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.addHeader("Auth", tokenDto.getAccessToken());
        response.addHeader("Refresh", tokenDto.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");

        PrintWriter writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(tokenDto));
        writer.flush();
    }
}
