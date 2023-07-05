package com.fanfixiv.auth.handler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fanfixiv.auth.dto.redis.RedisAuthDto;
import com.fanfixiv.auth.entity.UserEntity;
import com.fanfixiv.auth.interfaces.UserRoleEnum;
import com.fanfixiv.auth.repository.jpa.SecessionRepository;
import com.fanfixiv.auth.repository.jpa.UserRepository;
import com.fanfixiv.auth.repository.redis.RedisAuthRepository;

import com.fanfixiv.auth.utils.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final UserRepository userRepository;
  private final SecessionRepository secessionRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisAuthRepository redisAuthRepository;

  @Value("${micro.frontend.url}")
  private String url;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication)
      throws IOException, ServletException {
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

    String token = "";
    String refresh = "";
    boolean joined = false;

    UserEntity user = UserEntity.of(oAuth2User);
    if (!(secessionRepository.existsByEmail(user.getEmail()) &&
        secessionRepository.findByEmail(user.getEmail()).getSecDate().plusDays(30).isBefore(LocalDate.now()))) {

      if (!userRepository.existsByEmail(user.getEmail())) {
        userRepository.save(user);
      } else {
        user = userRepository.findByEmail(user.getEmail());
      }

      // 토큰 발급
      List<UserRoleEnum> roles = user.getRole().stream().map(item -> item.getRole()).toList();

      token = jwtTokenProvider.createToken(user.getSeq(), roles);
      refresh = jwtTokenProvider.createRefreshToken();

      redisAuthRepository.save(new RedisAuthDto(refresh, token));

      response.setHeader(HttpHeaders.SET_COOKIE, jwtTokenProvider.createRefreshTokenCookie(refresh).toString());

      joined = true;
    }

    // 리다이렉트 URL 발급
    String targetUrl;
    try {
      // 리다이렉트
      String path = "";

      URIBuilder builder = new URIBuilder(url);
      if (!(builder.isPathEmpty())) {
        path += builder.getPath();
      }

      targetUrl = builder
          .setPath(path + (joined ? "/twitter/login" : "/twitter/login-false"))
          .addParameter("token", token)
          .build()
          .toString();

    } catch (URISyntaxException e) {
      return;
    }

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}