package com.fanfixiv.auth.service;

import com.fanfixiv.auth.dto.login.LoginDto;
import com.fanfixiv.auth.dto.login.LoginResultDto;
import com.fanfixiv.auth.dto.redis.RedisAuthDto;
import com.fanfixiv.auth.entity.UserEntity;
import com.fanfixiv.auth.interfaces.UserRoleEnum;
import com.fanfixiv.auth.repository.RedisAuthRepository;
import com.fanfixiv.auth.repository.UserRepository;
import com.fanfixiv.auth.utils.JwtTokenProvider;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

  private final JwtTokenProvider jwtTokenProvider;

  private final UserRepository userRepository;

  private final RedisAuthRepository redisAuthRepository;

  private final BCryptPasswordEncoder passwordEncoder;

  @Transactional
  public LoginResultDto doLogin(HttpServletResponse response, LoginDto loginDto) throws Exception {

    if (!userRepository.existsByEmail(loginDto.getId())) {
      throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
    }

    UserEntity user = userRepository.findByEmail(loginDto.getId());

    if (!user.checkPassword(loginDto.getPw(), passwordEncoder)) {
      throw new BadCredentialsException("비밀번호가 불일치 합니다.");
    }

    List<UserRoleEnum> roles = user.getRole().stream().map(item -> item.getRole()).toList();

    String token = jwtTokenProvider.createToken(user.getSeq(), roles);
    String refresh = jwtTokenProvider.createRefreshToken();

    redisAuthRepository.save(RedisAuthDto.builder().refreshToken(refresh).jwtToken(token).build());

    response.setHeader("Set-Cookie", jwtTokenProvider.createRefreshTokenCookie(refresh).toString());

    return new LoginResultDto(token);
  }

  @Transactional
  public LoginResultDto refershToken(String refresh, String token) {
    Optional<RedisAuthDto> _authDto = redisAuthRepository.findById(refresh);
    token = jwtTokenProvider.bearerRemove(token);

    RedisAuthDto authDto = _authDto.orElseThrow(() -> new BadCredentialsException("토큰값이 올바르지 않습니다."));

    if (authDto.getJwtToken().equals(token)) {
      token = jwtTokenProvider.createTokenWithInVailedToken(token);
      redisAuthRepository.save(RedisAuthDto.builder().refreshToken(refresh).jwtToken(token).build());
      return new LoginResultDto(token);
    }

    throw new BadCredentialsException("토큰값이 올바르지 않습니다.");
  }
}
