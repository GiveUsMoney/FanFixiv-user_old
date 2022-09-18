package com.fanfixiv.auth.controller;

import com.fanfixiv.auth.dto.login.LoginDto;
import com.fanfixiv.auth.dto.login.LoginResultDto;
import com.fanfixiv.auth.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  @Autowired
  LoginService loginService;

  @PostMapping("/login")
  public LoginResultDto login(@RequestBody LoginDto loginDto) throws Exception {
    return loginService.doLogin(loginDto);
  }
}
