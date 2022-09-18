package com.fanfixiv.auth.config;

import com.fanfixiv.auth.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

  @Autowired private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public WebSecurityCustomizer configure() {
    // 해당 path들은 Security의 적용을 받지않음. -> 애초에 실제 배포 단계에서 있으면 안됨.
    return (web) -> web.ignoring().mvcMatchers("/swagger-ui.html");
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.httpBasic().disable(); // Http Basic을 이용한 보안 해제
    http.formLogin().disable(); // 기본 Login Form 해제
    http.cors().disable(); // CORS 보안 해제
    http.csrf().disable(); // CSRF 보안 해제
    http.sessionManagement()
        .sessionCreationPolicy(
            SessionCreationPolicy
                .STATELESS); // jwt token으로 인증하므로 stateless(인증정보를 서버에 남기지 않음) 하도록 처리.
    http.authorizeRequests()
        .antMatchers("/login", "/register")
        .permitAll() // 로그인 회원가입은 보안 해제
        .antMatchers("/test")
        .authenticated() // 테스트용 보안
        .anyRequest()
        .permitAll();

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
