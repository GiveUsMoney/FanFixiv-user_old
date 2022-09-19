package com.fanfixiv.auth.controller;

import com.fanfixiv.auth.dto.register.CertEmailResultDto;
import com.fanfixiv.auth.dto.register.DoubleCheckDto;
import com.fanfixiv.auth.dto.register.RegisterDto;
import com.fanfixiv.auth.dto.register.RegisterResultDto;
import com.fanfixiv.auth.service.RegisterService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/register")
public class RegisterController {

  @Autowired public RegisterService registerService;

  @PostMapping("/")
  public RegisterResultDto register(@RequestBody @Valid RegisterDto registerDto) {
    return new RegisterResultDto();
  }

  @GetMapping("/dc-nick")
  public DoubleCheckDto isNickDouble(@RequestParam String nickname) {
    return registerService.checkNickDouble(nickname);
  }

  @GetMapping("/cert-email")
  public CertEmailResultDto certEmail(@RequestParam String email) {
    return new CertEmailResultDto();
  }
}
