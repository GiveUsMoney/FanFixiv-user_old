package com.fanfixiv.auth.service;

import com.fanfixiv.auth.dto.register.DoubleCheckDto;
import com.fanfixiv.auth.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
  @Autowired private ProfileRepository profileRepository;

  public DoubleCheckDto checkNickDouble(String nickname) {
    return new DoubleCheckDto(profileRepository.existsByNickname(nickname));
  }
}
