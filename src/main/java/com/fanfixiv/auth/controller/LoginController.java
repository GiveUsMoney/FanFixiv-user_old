package com.fanfixiv.auth.controller;

import com.fanfixiv.auth.dto.login.LoginDto;
import com.fanfixiv.auth.dto.login.LoginResultDto;
import com.fanfixiv.auth.dto.profile.ProfileResultDto;
import com.fanfixiv.auth.service.LoginService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  @Autowired LoginService loginService;

  @PostMapping("/login")
  public LoginResultDto login(@RequestBody LoginDto loginDto) throws Exception {
    return loginService.doLogin(loginDto);
  }

  @GetMapping("/profile")
  public ProfileResultDto profile(Authentication authentication, Principal principal)
      throws Exception {
    return new ProfileResultDto(principal.getName());
  }
}
