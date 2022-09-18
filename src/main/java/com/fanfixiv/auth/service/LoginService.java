package com.fanfixiv.auth.service;

import com.fanfixiv.auth.dto.login.LoginDto;
import com.fanfixiv.auth.dto.login.LoginResultDto;
import com.fanfixiv.auth.entity.UserEntity;
import com.fanfixiv.auth.filter.JwtTokenProvider;
import com.fanfixiv.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

  @Autowired JwtTokenProvider jwtTokenProvider;

  @Autowired UserRepository userRepository;

  public LoginResultDto doLogin(LoginDto loginDto) throws Exception {

    if (userRepository.existsByEmail(loginDto.getId())) {
      UserEntity user = userRepository.findByEmail(loginDto.getId());
      String token = jwtTokenProvider.createToken(user.getSeq(), user.getRole());
      return new LoginResultDto(token);
    }
    throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
  }
}
