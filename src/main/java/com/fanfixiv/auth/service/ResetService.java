package com.fanfixiv.auth.service;

import java.util.Arrays;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fanfixiv.auth.dto.BaseResultDto;
import com.fanfixiv.auth.dto.redis.RedisResetDto;
import com.fanfixiv.auth.dto.register.CertEmailDto;
import com.fanfixiv.auth.dto.reset.ResetTokenDto;
import com.fanfixiv.auth.entity.UserEntity;
import com.fanfixiv.auth.exception.EmailNotExisitException;
import com.fanfixiv.auth.exception.TokenNotValidException;
import com.fanfixiv.auth.repository.RedisResetRepository;
import com.fanfixiv.auth.repository.UserRepository;
import com.fanfixiv.auth.utils.RandomProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResetService {

  private final UserRepository userRepository;

  private final RedisResetRepository redisResetRepository;

  private final MailService mailService;

  private final BCryptPasswordEncoder passwordEncoder;

  public BaseResultDto resetEmail(CertEmailDto dto) {

    if (userRepository.existsByEmail(dto.getEmail())) {
      String uuid = RandomProvider.getUUID();

      RedisResetDto rDto = RedisResetDto.builder()
          .uuid(uuid)
          .email(dto.getEmail())
          .build();

      redisResetRepository.save(rDto);

      mailService.sendResetPwMail(uuid, Arrays.asList(dto.getEmail()));

      return new BaseResultDto();
    }

    throw new EmailNotExisitException("이메일이 존재하지 않습니다.");

  }

  @Transactional
  public BaseResultDto resetPw(ResetTokenDto dto) {

    Optional<RedisResetDto> _rDto = redisResetRepository.findById(dto.getToken());

    RedisResetDto rDto = _rDto.orElseThrow(() -> new TokenNotValidException("토큰값이 올바르지 않습니다."));

    redisResetRepository.deleteById(rDto.getUuid());

    UserEntity user = userRepository.findByEmail(rDto.getEmail());

    user.setPw(dto.getPw());

    user.hashPassword(passwordEncoder);

    userRepository.save(user);

    return new BaseResultDto();
  }

}
