package com.fanfixiv.auth.dto.secession;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SecessionDto {
  @NotEmpty
  private String pw;
}
