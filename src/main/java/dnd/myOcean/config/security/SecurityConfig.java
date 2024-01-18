package dnd.myOcean.config.security;

import dnd.myOcean.domain.member.RoleType;
import dnd.myOcean.service.sign.KakaoMemberDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final KakaoMemberDetailService kakaoMemberDetailService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests((authorizeRequests) -> {
                    authorizeRequests
                            .requestMatchers("/api/login/").permitAll()
                            .requestMatchers("/api/member/**").authenticated()
                            .requestMatchers("/api/admin/**").hasRole(RoleType.ADMIN.name())
                            .anyRequest().permitAll();
                })

                .oauth2Login(oAuth2Login ->
                        oAuth2Login.userInfoEndpoint(userInfoEndpointConfig ->
                                        userInfoEndpointConfig.userService(kakaoMemberDetailService))
                                .loginPage("/login"));

        return http.build();
    }
}
