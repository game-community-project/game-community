package com.gamecommunity.global.config;


import com.gamecommunity.domain.user.entity.User;
import com.gamecommunity.global.exception.common.BusinessException;
import com.gamecommunity.global.exception.common.ErrorCode;
import com.gamecommunity.global.security.filter.JwtAuthorizationFilter;
import com.gamecommunity.global.security.userdetails.UserDetailsImpl;
import com.gamecommunity.global.security.userdetails.UserDetailsServiceImpl;
import com.gamecommunity.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.disable());

        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/signup").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/teams/**").permitAll()
                        .requestMatchers("/api/posts/**").permitAll()
                        .anyRequest().authenticated()
        );

//        http.formLogin((formLogin) ->
//                formLogin
//                        .loginPage("/login").permitAll()
//        );


        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 비회원에게 허용되지 않은 API를 사용할 때 예외처리
    @Bean
    public AuthenticationHelper authenticationHelper() {
        return new AuthenticationHelper();
    }

    public static class AuthenticationHelper {

        public User checkAuthentication(UserDetailsImpl userDetails) {
            if (userDetails == null) {
                throw new BusinessException(HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION_EXCEPTION);
            }

            return userDetails.getUser();
        }
    }

}
