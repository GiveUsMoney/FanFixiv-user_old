package com.fanfixiv.auth.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@RedisHash(value = "email", timeToLive= 60 * 60)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedisEmailAuthDto {
  @Id
  private String uuid;
  private String email;
  private String number;
  private boolean success;
  private LocalDateTime expireTime;
}
