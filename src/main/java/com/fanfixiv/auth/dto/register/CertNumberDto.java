package com.fanfixiv.auth.dto.register;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertNumberDto {
  @NotEmpty
  private String uuid;

  @NotEmpty
  private String number;
}
