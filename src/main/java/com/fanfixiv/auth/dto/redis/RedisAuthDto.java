package com.fanfixiv.auth.dto.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@RedisHash(value = "auth", timeToLive = 60 * 60 * 24 * 14)
@NoArgsConstructor
@AllArgsConstructor
public class RedisAuthDto {
  @Id
  private String refreshToken;
  private String jwtToken;
}
