package dnd.myOcean.config.security;

import dnd.myOcean.config.security.details.oauth2.OAuth2UserService;
import dnd.myOcean.domain.member.RoleType;
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

    private final OAuth2UserService oAuth2UserService;

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
                            .requestMatchers("/api/login/**", "/api/join/**").permitAll()
                            .requestMatchers("/api/member/**").authenticated()
                            .requestMatchers("/api/admin/**").hasRole(RoleType.ADMIN.name())
                            .anyRequest().permitAll();
                })

                .formLogin((formLogin) -> {
                    formLogin
                            .loginProcessingUrl("/api/login") // '/login' URI 호출 시 시큐리티가 낚아채서 로그인 진행
                            .defaultSuccessUrl("/");
                })

                .oauth2Login((oauth2Login) -> oauth2Login
                        .userInfoEndpoint(
                                (userInfoEndpointConfig -> userInfoEndpointConfig
                                        .userService(oAuth2UserService))));

        return http.build();
    }
}
