package com.fanfixiv.auth.filter;

import java.io.IOException;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

  @Autowired private JwtTokenProvider jwtTokenProvider;

  @Autowired private RedisTemplate<String, String> redisTemplate;

  @Override
  public void doFilter(ServletRequest request, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    String token = jwtTokenProvider.resolveToken(request);
    String refresh = jwtTokenProvider.resolveRefreshToken(request);

    HttpServletResponse response = (HttpServletResponse) res;

    if (!"OPTIONS".equals(((HttpServletRequest) request).getMethod())) {
      if (token != null && refresh != null) {
        // JWT 토큰과 Refresh 토큰이 만료되지 않음.
        if (jwtTokenProvider.validateToken(refresh) && jwtTokenProvider.validateToken(token)) {
          Authentication authentication = jwtTokenProvider.getAuthentication(token);
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // JWT 토큰이 만료되었으나 refershToken이 만료되지 않음
        else if (jwtTokenProvider.validateToken(refresh)) {
          if (redisTemplate.opsForValue().get(refresh).equals(token)) {
            token = jwtTokenProvider.createTokenWithInVailedToken(token);
            redisTemplate.opsForValue().set(refresh, token);
            redisTemplate.expireAt(refresh, new Date(new Date().getTime() + 60 * 60 * 1000));
            response.setHeader("Authorization", token);

            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
          }
        }
        // JWT 토큰이 만료되지 않았으나 refershToken이 만료됨
        else if (jwtTokenProvider.validateToken(token)) {
          String value = redisTemplate.opsForValue().get(refresh);
          if (value != null && value.equals(token)) {
            redisTemplate.delete(refresh);
            refresh = jwtTokenProvider.createRefreshToken();
            redisTemplate.opsForValue().set(refresh, token);
            redisTemplate.expireAt(refresh, new Date(new Date().getTime() + 60 * 60 * 1000));
            response.setHeader(
                "Set-Cookie", jwtTokenProvider.createRefreshTokenCookie(refresh).toString());

            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
          }
        }
      }
    }

    chain.doFilter(request, response);
  }
}
