package com.fanfixiv.auth.dto.register;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CertNumberDto {
  @NotEmpty
  private String uuid;

  @NotEmpty
  private String number;
}
