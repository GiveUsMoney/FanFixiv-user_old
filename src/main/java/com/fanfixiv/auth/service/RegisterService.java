package com.fanfixiv.auth.service;

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
import com.fanfixiv.auth.repository.UserRepository;
import com.fanfixiv.auth.utils.RandomProvider;
import com.fanfixiv.auth.utils.TimeProvider;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {
  private final ProfileRepository profileRepository;

  private final UserRepository userRepository;

  private final RedisTemplate<String, String> redisTemplate;

  private final MailService mailService;

  private final BCryptPasswordEncoder passwordEncoder;

  public RegisterResultDto register(RegisterDto dto) {
    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new DuplicateException("이미 사용중인 이메일입니다.");
    }
    if (profileRepository.existsByNickname(dto.getNickname())) {
      throw new DuplicateException("이미 사용중인 닉네임입니다.");
    }
    String success = redisTemplate.opsForValue().get(dto.getUuid());
    if (success == null || !"success".equals(success)) {
      throw new DuplicateException("본인인증이 되어있지 않습니다.");
    }

    ProfileEntity profile =
        ProfileEntity.builder().nickname(dto.getNickname()).is_tr(false).build();

    UserEntity user =
        UserEntity.builder().email(dto.getEmail()).pw(dto.getPw()).profile(profile).build();

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

    ValueOperations<String, String> vo = redisTemplate.opsForValue();
    vo.set(uuid, number);
    redisTemplate.expireAt(uuid, java.sql.Timestamp.valueOf(expireTime));

    return new CertEmailResultDto(uuid, expireTime);
  }

  public CertNumberResultDto certNumber(CertNumberDto dto) {
    String number = redisTemplate.opsForValue().get(dto.getUuid());

    if (number == null) {
      return new CertNumberResultDto(false);
    }

    boolean result = number.equals(dto.getNumber());

    if (result) {
      this.redisTemplate.opsForValue().set(dto.getUuid(), "success");
      this.redisTemplate.expireAt(
          dto.getUuid(), java.sql.Timestamp.valueOf(TimeProvider.getTimeAfter1hour()));
    }

    return new CertNumberResultDto(result);
  }
}
