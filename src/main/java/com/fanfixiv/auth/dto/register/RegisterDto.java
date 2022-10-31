package com.fanfixiv.auth.dto.register;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
  @NotEmpty
  private String pw;

  @NotEmpty
  private String nickname;

  @NotEmpty
  private String birth;

  @NotEmpty
  private String uuid;

  private String profileImg;
}
