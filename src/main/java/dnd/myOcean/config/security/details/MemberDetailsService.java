package dnd.myOcean.config.security.details;

import dnd.myOcean.domain.member.Member;
import dnd.myOcean.exception.MemberNotFoundException;
import dnd.myOcean.repository.member.MemberRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username = {}", username);

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(MemberNotFoundException::new);

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(member.getRoleType().name());

        return new MemberDetails(String.valueOf(member.getId()),
                member.getUsername(),
                member.getPassword(),
                Collections.singletonList(authority),
                Collections.emptyMap());
    }
}
