package com.fanfixiv.auth.service;

import com.fanfixiv.auth.dto.redis.RedisEmailAuthDto;
import com.fanfixiv.auth.dto.register.CertEmailResultDto;
import com.fanfixiv.auth.dto.register.CertNumberDto;
import com.fanfixiv.auth.dto.register.CertNumberResultDto;
import com.fanfixiv.auth.dto.register.DoubleCheckDto;
import com.fanfixiv.auth.dto.register.RegisterDto;
import com.fanfixiv.auth.dto.register.RegisterResultDto;
import com.fanfixiv.auth.entity.ProfileEntity;
import com.fanfixiv.auth.entity.RoleEntity;
import com.fanfixiv.auth.entity.UserEntity;
import com.fanfixiv.auth.exception.DuplicateException;
import com.fanfixiv.auth.exception.SecessionAccountExcetpion;
import com.fanfixiv.auth.repository.ProfileRepository;
import com.fanfixiv.auth.repository.RedisEmailRepository;
import com.fanfixiv.auth.repository.SecessionRepository;
import com.fanfixiv.auth.repository.UserRepository;
import com.fanfixiv.auth.requester.RestRequester;
import com.fanfixiv.auth.utils.RandomProvider;
import com.fanfixiv.auth.utils.TimeProvider;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {
  private final ProfileRepository profileRepository;

  private final UserRepository userRepository;

  private final SecessionRepository secessionRepository;

  private final RedisEmailRepository redisEmailRepository;

  private final MailService mailService;

  private final BCryptPasswordEncoder passwordEncoder;

  private final RestRequester restRequester;

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Transactional
  public RegisterResultDto register(RegisterDto dto) {
    if (profileRepository.existsByNickname(dto.getNickname())) {
      throw new DuplicateException("이미 사용중인 닉네임입니다.");
    }

    Optional<RedisEmailAuthDto> _redisDto = redisEmailRepository.findById(dto.getUuid());
    RedisEmailAuthDto redisDto = _redisDto.orElseThrow(() -> new DuplicateException("본인인증이 되어있지 않습니다."));

    if (userRepository.existsByEmail(redisDto.getEmail())) {
      throw new DuplicateException("이미 사용중인 이메일입니다.");
    }
    if (!redisDto.isSuccess()) {
      throw new DuplicateException("본인인증이 되어있지 않습니다.");
    }
    if (secessionRepository.existsByEmail(redisDto.getEmail()) &&
        secessionRepository.findByEmail(redisDto.getEmail()).getSecDate().plusDays(30).isAfter(LocalDate.now())) {
      throw new SecessionAccountExcetpion("탈퇴한 계정은 30일간 재가입 할수 없습니다.");
    }

    String profileImgUrl = "";
    if (dto.getProfileImg() != null) {
      profileImgUrl = restRequester.uploadProfileImg(dto.getProfileImg());
    }

    LocalDate birth = LocalDate.parse(dto.getBirth(), this.formatter);

    ProfileEntity profile = ProfileEntity.builder()
        .nickname(dto.getNickname())
        .isTr(false)
        .birth(birth)
        .profileImg(profileImgUrl)
        .build();

    UserEntity user = UserEntity.builder()
        .email(redisDto.getEmail())
        .pw(dto.getPw())
        .profile(profile)
        .role(Arrays.asList(new RoleEntity()))
        .build();

    user.hashPassword(passwordEncoder);

    userRepository.save(user);

    return new RegisterResultDto("성공적으로 회원가입되었습니다.");
  }

  @Transactional
  public DoubleCheckDto checkNickDouble(String nickname) {
    return new DoubleCheckDto(!profileRepository.existsByNickname(nickname));
  }

  @Transactional
  public CertEmailResultDto certEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new DuplicateException("이미 사용중인 이메일입니다.");
    }

    String uuid = RandomProvider.getUUID();
    String number = RandomProvider.getRandomNumber();
    LocalDateTime expireTime = new Timestamp(TimeProvider.getTimeAfter3min().getTime()).toLocalDateTime();

    List<String> sendTo = Arrays.asList(email);

    mailService.sendEmailAuthMail(number, sendTo);

    RedisEmailAuthDto rDto = RedisEmailAuthDto.builder()
        .uuid(uuid)
        .email(email)
        .number(number)
        .expireTime(expireTime)
        .build();

    redisEmailRepository.save(rDto);

    return new CertEmailResultDto(uuid, expireTime);
  }

  @Transactional
  public CertNumberResultDto certNumber(CertNumberDto dto) {
    Optional<RedisEmailAuthDto> redisDtoOptional = redisEmailRepository.findById(dto.getUuid());

    if (redisDtoOptional.isPresent()) {
      RedisEmailAuthDto redisDto = redisDtoOptional.get();
      String number = redisDto.getNumber();
      LocalDateTime expire = redisDto.getExpireTime();

      if (number.equals(dto.getNumber()) && expire.isAfter(LocalDateTime.now())) {
        redisDto.setSuccess(true);
        redisEmailRepository.save(redisDto);
        return new CertNumberResultDto(true);
      }
    }

    return new CertNumberResultDto(false);
  }
}
