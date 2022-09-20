package com.fanfixiv.auth.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResultDto {
  private String token;
}
