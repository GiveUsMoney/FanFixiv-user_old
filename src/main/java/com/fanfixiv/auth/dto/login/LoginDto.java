package com.fanfixiv.auth.dto.login;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

  @NotEmpty
  private String id;

  @NotEmpty
  private String pw;
}
