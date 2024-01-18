package dnd.myOcean.config.security.details.oauth2;

import dnd.myOcean.config.security.details.MemberDetails;
import dnd.myOcean.domain.member.Member;
import dnd.myOcean.repository.member.MemberRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());

        log.info("kakaoUserInfo.getEmail()= {}", kakaoUserInfo.getEmail());

        Member member = memberRepository.findByUsername(kakaoUserInfo.getEmail())
                .orElseGet(() -> memberRepository.save(
                        new Member(kakaoUserInfo.getEmail(), "", kakaoUserInfo.getEmail())));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(member.getRoleType().name());

        return new MemberDetails(String.valueOf(member.getId()), member.getUsername(), "",
                Collections.singletonList(authority), oAuth2User.getAttributes());
    }
}
