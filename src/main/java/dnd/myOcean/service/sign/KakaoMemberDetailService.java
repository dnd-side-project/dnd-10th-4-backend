package dnd.myOcean.service.sign;


import dnd.myOcean.config.security.details.KakaoUserInfo;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoMemberDetailService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());

        Member member = memberRepository.findByUsername(kakaoUserInfo.getEmail())
                .orElseGet(() ->
                        memberRepository.save(
                                Member.builder()
                                        .username(kakaoUserInfo.getEmail())
                                        .nickname("temp")
                                        .password("")
                                        .build()
                        )
                );

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(member.getRoleType().name());

        return new MemberDetails(String.valueOf(member.getId()), member.getUsername(), "",
                Collections.singletonList(authority), oAuth2User.getAttributes());
    }
}
