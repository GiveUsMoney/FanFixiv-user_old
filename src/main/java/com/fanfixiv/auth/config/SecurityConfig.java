package com.fanfixiv.auth.config;

import com.fanfixiv.auth.filter.CustomRequestEntityConverter;
import com.fanfixiv.auth.filter.CustomTokenResponseConverter;
import com.fanfixiv.auth.filter.JwtAuthenticationFilter;
import com.fanfixiv.auth.handler.CustomAuthenticationEntryPoint;
import com.fanfixiv.auth.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  private final CustomOAuth2UserService customOAuth2UserService;

  @Bean
  public WebSecurityCustomizer configure() {
    // 해당 path들은 Security의 적용을 받지않음.
    // swagger가 포함됨
    return (web) -> web.ignoring()
        .mvcMatchers(
            "/swagger-ui.html/**", "/webjars/**", "/swagger-resources/**", "/v2/api-docs");
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    return new CustomAuthenticationEntryPoint();
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(
        Arrays.asList("http://localhost:3000/", "https://giveusmoney.github.io/"));
    configuration.setAllowedMethods(Arrays.asList("*"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
    DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
    accessTokenResponseClient.setRequestEntityConverter(new CustomRequestEntityConverter());

    OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter = new OAuth2AccessTokenResponseHttpMessageConverter();
    tokenResponseHttpMessageConverter.setAccessTokenResponseConverter(new CustomTokenResponseConverter());

    RestTemplate restTemplate = new RestTemplate(Arrays.asList(
        new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
    restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

    accessTokenResponseClient.setRestOperations(restTemplate);
    return accessTokenResponseClient;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.httpBasic().disable(); // Http Basic을 이용한 보안 해제
    http.formLogin().disable(); // 기본 Login Form 해제
    http.cors();
    http.csrf().disable(); // CSRF 보안 해제
    http.sessionManagement()
        .sessionCreationPolicy(
            SessionCreationPolicy.STATELESS); // jwt token으로 인증하므로 stateless(인증정보를 서버에 남기지 않음) 하도록 처리.

    http.authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS, "/**")
        .permitAll()
        .antMatchers("/", "/login", "/register/**", "/refresh")
        .permitAll(); // 로그인 회원가입은 보안 해제

    http.authorizeRequests()
        .antMatchers("/roles/user")
        .hasRole("USER")
        .antMatchers("/roles/artist")
        .hasRole("ARTIST")
        .antMatchers("/roles/trans")
        .hasRole("TRANSLATER")
        .antMatchers("/roles/admin")
        .hasRole("ADMIN");

    http.authorizeRequests()
        .anyRequest()
        .authenticated();

    http.oauth2Login()
        .redirectionEndpoint()
        .baseUri("/oauth2/code/*")
        .and()
        .tokenEndpoint()
        .accessTokenResponseClient(accessTokenResponseClient())
        .and()
        .userInfoEndpoint()
        .userService(customOAuth2UserService);

    http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
