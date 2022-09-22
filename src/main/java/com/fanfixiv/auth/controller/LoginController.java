package com.fanfixiv.auth.controller;

import com.fanfixiv.auth.dto.login.LoginDto;
import com.fanfixiv.auth.dto.login.LoginResultDto;
import com.fanfixiv.auth.dto.profile.ProfileResultDto;
import com.fanfixiv.auth.service.LoginService;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
public class LoginController {

  private final LoginService loginService;

  @PostMapping("/login")
  public LoginResultDto login(HttpServletResponse response, @RequestBody @Valid LoginDto loginDto)
      throws Exception {
    return loginService.doLogin(response, loginDto);
  }

  @GetMapping("/profile")
  public ProfileResultDto profile(Authentication authentication, Principal principal)
      throws Exception {
    return new ProfileResultDto(principal.getName());
  }
}
