package com.fanfixiv.auth.dto.register;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
  @NotEmpty private String pw;

  @NotEmpty private String nickname;

  @NotEmpty private String uuid;
}

