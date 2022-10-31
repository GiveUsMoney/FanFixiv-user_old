package com.fanfixiv.auth.dto.register;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CertEmailDto {
  @NotEmpty
  @Email
  private String email;
}
