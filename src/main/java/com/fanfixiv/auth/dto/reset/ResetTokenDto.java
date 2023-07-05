package com.fanfixiv.auth.dto.reset;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResetTokenDto {
  @NotEmpty
  private String token;
  @NotEmpty
  private String pw;
}
