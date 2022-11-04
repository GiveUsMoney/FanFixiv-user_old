package com.fanfixiv.auth.service;

import com.fanfixiv.auth.details.User;
import com.fanfixiv.auth.dto.BaseResultDto;
import com.fanfixiv.auth.dto.login.LoginDto;
import com.fanfixiv.auth.dto.login.LoginResultDto;
import com.fanfixiv.auth.dto.login.LogoutResultDto;
import com.fanfixiv.auth.dto.redis.RedisAuthDto;
import com.fanfixiv.auth.dto.secession.SecessionDto;
import com.fanfixiv.auth.entity.SecessionEntity;
import com.fanfixiv.auth.entity.UserEntity;
import com.fanfixiv.auth.exception.BadRequestException;
import com.fanfixiv.auth.exception.UnauthorizedException;
import com.fanfixiv.auth.interfaces.UserRoleEnum;
import com.fanfixiv.auth.repository.RedisAuthRepository;
import com.fanfixiv.auth.repository.SecessionRepository;
import com.fanfixiv.auth.repository.UserRepository;
import com.fanfixiv.auth.utils.JwtTokenProvider;
import com.google.common.net.HttpHeaders;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

  private final JwtTokenProvider jwtTokenProvider;

  private final UserRepository userRepository;

  private final SecessionRepository secessionRepository;

  private final RedisAuthRepository redisAuthRepository;

  private final BCryptPasswordEncoder passwordEncoder;

  @Transactional
  public LoginResultDto doLogin(HttpServletResponse response, LoginDto loginDto) throws Exception {

    if (!userRepository.existsByEmail(loginDto.getId())) {
      throw new UnauthorizedException("사용자를 찾을수 없습니다.");
    }

    UserEntity user = userRepository.findByEmail(loginDto.getId());

    if (!user.checkPassword(loginDto.getPw(), passwordEncoder)) {
      throw new UnauthorizedException("비밀번호가 불일치 합니다.");
    }

    List<UserRoleEnum> roles = user.getRole().stream().map(item -> item.getRole()).toList();

    String token = jwtTokenProvider.createToken(user.getSeq(), roles);
    String refresh = jwtTokenProvider.createRefreshToken();

    redisAuthRepository.save(new RedisAuthDto(refresh, token));

    response.setHeader(HttpHeaders.SET_COOKIE, jwtTokenProvider.createRefreshTokenCookie(refresh).toString());
    return new LoginResultDto(token);
  }

  public LogoutResultDto doLogout(String refresh, String token) throws Exception {

    Optional<RedisAuthDto> _authDto = redisAuthRepository.findById(refresh);
    RedisAuthDto authDto = _authDto.orElseThrow(() -> new UnauthorizedException("토큰값이 올바르지 않습니다."));

    token = jwtTokenProvider.bearerRemove(token);

    if (authDto.getJwtToken().equals(token)) {
      redisAuthRepository.delete(authDto);
      return new LogoutResultDto(true);
    }

    throw new UnauthorizedException("토큰값이 올바르지 않습니다.");
  }

  @Transactional
  public LoginResultDto refershToken(String refresh, String token) {
    Optional<RedisAuthDto> _authDto = redisAuthRepository.findById(refresh);
    token = jwtTokenProvider.bearerRemove(token);

    RedisAuthDto authDto = _authDto.orElseThrow(() -> new UnauthorizedException("토큰값이 올바르지 않습니다."));

    if (authDto.getJwtToken().equals(token)) {
      token = jwtTokenProvider.createTokenWithInVailedToken(token);
      redisAuthRepository.save(new RedisAuthDto(refresh, token));
      return new LoginResultDto(token);
    }

    throw new UnauthorizedException("토큰값이 올바르지 않습니다.");
  }

  @Transactional
  public BaseResultDto doSecession(User user, SecessionDto dto) {
    Long seq = user.getUserSeq();
    String email = user.getUserEmail();

    UserEntity userEntity = userRepository.findById(seq).get();

    if (dto == null && userEntity.getPw() != null) {
      throw new BadRequestException("비밀번호를 입력해주세요.");
    }

    if (dto != null && !userEntity.checkPassword(dto.getPw(), passwordEncoder)) {
      throw new BadRequestException("비밀번호가 일치하지 않습니다.");
    }

    userRepository.delete(userEntity);
    secessionRepository.save(new SecessionEntity(email));

    return new BaseResultDto();
  }
}
