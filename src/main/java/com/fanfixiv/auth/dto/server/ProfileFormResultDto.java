package com.fanfixiv.auth.dto.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileFormResultDto extends BaseResultFormDto {
  private String key;
}
