package com.fanfixiv.auth.dto.login;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

  @NotEmpty private String id;
  @NotEmpty private String pw;
}
