package com.fanfixiv.auth.service;

import com.fanfixiv.auth.dto.login.LoginDto;
import com.fanfixiv.auth.dto.login.LoginResultDto;
import com.fanfixiv.auth.entity.UserEntity;
import com.fanfixiv.auth.filter.JwtTokenProvider;
import com.fanfixiv.auth.repository.UserRepository;
import com.fanfixiv.auth.utils.HashProvider;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  @Autowired JwtTokenProvider jwtTokenProvider;

  @Autowired UserRepository userRepository;

  @Autowired RedisTemplate<String, String> redisTemplate;

  public LoginResultDto doLogin(HttpServletResponse response, LoginDto loginDto) throws Exception {

    if (!userRepository.existsByEmail(loginDto.getId())) {
      throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
    }

    UserEntity user = userRepository.findByEmail(loginDto.getId());

    String salt = user.getSalt();

    String pw = HashProvider.hashString(loginDto.getPw(), salt);

    if (!user.getPw().equals(pw)) {
      throw new BadCredentialsException("비밀번호가 불일치 합니다.");
    }

    String token = jwtTokenProvider.createToken(user.getSeq(), user.getRole());
    String refresh = jwtTokenProvider.createRefreshToken();

    redisTemplate.opsForValue().set(refresh, token);
    redisTemplate.expireAt(refresh, new Date(new Date().getTime() + 60 * 60 * 1000));

    response.setHeader("Set-Cookie", jwtTokenProvider.createRefreshTokenCookie(refresh).toString());

    return new LoginResultDto(token);
  }
}
