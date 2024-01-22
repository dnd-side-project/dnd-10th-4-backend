package dnd.myOcean.config.oAuth.kakao.details;

import dnd.myOcean.domain.member.Gender;
import dnd.myOcean.domain.member.Member;
import dnd.myOcean.domain.member.Role;
import dnd.myOcean.repository.MemberRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KakaoMemberDetailsService extends DefaultOAuth2UserService {

    private static final String PREFIX = "낯선 ";


    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());

        Member member = memberRepository.findByEmail(kakaoUserInfo.getEmail())
                .orElseGet(() ->
                        memberRepository.save(
                                Member.builder()
                                        .email(kakaoUserInfo.getEmail())
                                        .role(Role.USER)
                                        .nickName(PREFIX)
                                        .gender(Gender.NONE)
                                        .updatedBirthday(false)
                                        .updatedGender(false)
                                        .build()
                        )
                );

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(member.getRole().name());

        return new KakaoMemberDetails(String.valueOf(member.getEmail()),
                Collections.singletonList(authority),
                oAuth2User.getAttributes());
    }
}
