package com.fanfixiv.auth.dto.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RedisHash(value = "reset", timeToLive = 10 * 60)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedisResetDto {
  @Id
  private String uuid;

  private String email;
}
