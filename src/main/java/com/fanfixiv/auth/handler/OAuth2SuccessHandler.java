package com.fanfixiv.auth.handler;

import java.io.IOException;
import java.net.URISyntaxException;
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
import com.fanfixiv.auth.repository.RedisAuthRepository;
import com.fanfixiv.auth.repository.UserRepository;
import com.fanfixiv.auth.utils.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisAuthRepository redisAuthRepository;

  @Value("${micro.frontend.url}")
  private String url;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication)
      throws IOException, ServletException {
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

    String birth = "true";

    UserEntity user = UserEntity.of(oAuth2User);
    if (!userRepository.existsByEmail(user.getEmail())) {
      userRepository.save(user);
      birth = "false";
    }
    user = userRepository.findByEmail(user.getEmail());

    // 토큰 발급
    List<UserRoleEnum> roles = user.getRole().stream().map(item -> item.getRole()).toList();

    String token = jwtTokenProvider.createToken(user.getSeq(), roles);
    String refresh = jwtTokenProvider.createRefreshToken();

    redisAuthRepository.save(RedisAuthDto.builder().refreshToken(refresh).jwtToken(token).build());

    response.setHeader(HttpHeaders.SET_COOKIE, jwtTokenProvider.createRefreshTokenCookie(refresh).toString());

    // 리다이렉트 URL 발급
    String targetUrl;
    try {
      // 리다이렉트
      targetUrl = new URIBuilder(url)
          .setPath("twitter/login")
          .addParameter("token", token)
          .addParameter("birth", birth)
          .build()
          .toString();

    } catch (URISyntaxException e) {
      return;
    }

    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}