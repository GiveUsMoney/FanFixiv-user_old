package com.fanfixiv.auth.dto.server;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileFormResultDto extends BaseResultFormDto {
  private String key;
}
