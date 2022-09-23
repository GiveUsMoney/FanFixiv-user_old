package com.fanfixiv.auth.service;

import com.fanfixiv.auth.dto.redis.RedisEmailAuthDto;
import com.fanfixiv.auth.dto.register.CertEmailResultDto;
import com.fanfixiv.auth.dto.register.CertNumberDto;
import com.fanfixiv.auth.dto.register.CertNumberResultDto;
import com.fanfixiv.auth.dto.register.DoubleCheckDto;
import com.fanfixiv.auth.dto.register.RegisterDto;
import com.fanfixiv.auth.dto.register.RegisterResultDto;
import com.fanfixiv.auth.entity.ProfileEntity;
import com.fanfixiv.auth.entity.UserEntity;
import com.fanfixiv.auth.exception.DuplicateException;
import com.fanfixiv.auth.repository.ProfileRepository;
import com.fanfixiv.auth.repository.RedisEmailRepository;
import com.fanfixiv.auth.repository.UserRepository;
import com.fanfixiv.auth.utils.RandomProvider;
import com.fanfixiv.auth.utils.TimeProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
  @Autowired private ProfileRepository profileRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private RedisEmailRepository redisEmailRepository;

  @Autowired private MailService mailService;

  @Autowired private BCryptPasswordEncoder passwordEncoder;

  public RegisterResultDto register(RegisterDto dto) {
    if (profileRepository.existsByNickname(dto.getNickname())) {
      throw new DuplicateException("이미 사용중인 닉네임입니다.");
    }

    Optional<RedisEmailAuthDto> _redisDto = redisEmailRepository.findById(dto.getUuid());
    _redisDto.orElseThrow(() -> new DuplicateException("본인인증이 되어있지 않습니다."));
    RedisEmailAuthDto redisDto = _redisDto.get();

    if (userRepository.existsByEmail(redisDto.getEmail())) {
      throw new DuplicateException("이미 사용중인 이메일입니다.");
    }
    if (redisDto.isSuccess()) {
      new DuplicateException("본인인증이 되어있지 않습니다.");
    }

    ProfileEntity profile =
        ProfileEntity.builder().nickname(dto.getNickname()).is_tr(false).build();

    UserEntity user =
        UserEntity.builder().email(redisDto.getEmail()).pw(dto.getPw()).profile(profile).build();

    user.hashPassword(passwordEncoder);

    userRepository.save(user);

    return new RegisterResultDto("성공적으로 회원가입되었습니다.");
  }

  public DoubleCheckDto checkNickDouble(String nickname) {
    return new DoubleCheckDto(profileRepository.existsByNickname(nickname));
  }

  public CertEmailResultDto certEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new DuplicateException("이미 사용중인 이메일입니다.");
    }

    String uuid = RandomProvider.getUUID();
    String number = RandomProvider.getRandomNumber();
    LocalDateTime expireTime = TimeProvider.getTimeAfter3min();

    List<String> sendTo =
        new ArrayList<String>() {
          {
            add(email);
          }
        };

    mailService.sendMail("회원가입 이메일", number, sendTo);

    RedisEmailAuthDto rDto = RedisEmailAuthDto.builder()
    .uuid(uuid)
    .email(email)
    .number(number)
    .expireTime(expireTime)
    .build();

    redisEmailRepository.save(rDto);

    return new CertEmailResultDto(uuid, expireTime);
  }

  public CertNumberResultDto certNumber(CertNumberDto dto) {
    Optional<RedisEmailAuthDto> redisDtoOptional = redisEmailRepository.findById(dto.getUuid());

    if(redisDtoOptional.isPresent()) {
      RedisEmailAuthDto redisDto = redisDtoOptional.get();
      String number = redisDto.getNumber();      
      LocalDateTime expire = redisDto.getExpireTime();
  
      if(number.equals(dto.getNumber()) && expire.isAfter(LocalDateTime.now())) {
        redisDto.setSuccess(true);
        redisEmailRepository.save(redisDto);
        return new CertNumberResultDto(true);
      }
    }


    return new CertNumberResultDto(false);
  }
}
