package com.fanfixiv.auth.service;

import com.fanfixiv.auth.dto.login.LoginDto;
import com.fanfixiv.auth.dto.login.LoginResultDto;
import com.fanfixiv.auth.entity.UserEntity;
import com.fanfixiv.auth.filter.JwtTokenProvider;
import com.fanfixiv.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  @Autowired JwtTokenProvider jwtTokenProvider;

  @Autowired UserRepository userRepository;

  @Autowired BCryptPasswordEncoder passwordEncoder;

  public LoginResultDto doLogin(LoginDto loginDto) throws Exception {

    if (!userRepository.existsByEmail(loginDto.getId())) {
      throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
    }

    UserEntity user = userRepository.findByEmail(loginDto.getId());

    if (!user.checkPassword(loginDto.getPw(), passwordEncoder)) {
      throw new BadCredentialsException("비밀번호가 불일치 합니다.");
    }

    String token = jwtTokenProvider.createToken(user.getSeq(), user.getRole());
    return new LoginResultDto(token);
  }
}
