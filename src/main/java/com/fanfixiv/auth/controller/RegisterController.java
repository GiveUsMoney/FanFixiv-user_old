package com.fanfixiv.auth.controller;

import com.fanfixiv.auth.dto.register.CertEmailDto;
import com.fanfixiv.auth.dto.register.CertEmailResultDto;
import com.fanfixiv.auth.dto.register.CertNumberDto;
import com.fanfixiv.auth.dto.register.CertNumberResultDto;
import com.fanfixiv.auth.dto.register.DoubleCheckDto;
import com.fanfixiv.auth.dto.register.RegisterDto;
import com.fanfixiv.auth.dto.register.RegisterResultDto;
import com.fanfixiv.auth.service.RegisterService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

  private final RegisterService registerService;

  @PostMapping("")
  @ResponseStatus(code = HttpStatus.CREATED)
  public RegisterResultDto register(@RequestBody @Valid RegisterDto registerDto) {
    return registerService.register(registerDto);
  }

  @GetMapping("/dc-nick")
  public DoubleCheckDto isNickDouble(@RequestParam String nickname) {
    return registerService.checkNickDouble(nickname);
  }

  @GetMapping("/cert-email")
  public CertEmailResultDto certEmail(@Valid CertEmailDto dto) {
    return registerService.certEmail(dto.getEmail());
  }

  @GetMapping("/cert-number")
  public CertNumberResultDto certNumber(@Valid CertNumberDto dto) {
    return registerService.certNumber(dto);
  }
}
