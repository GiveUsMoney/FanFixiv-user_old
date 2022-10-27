package com.fanfixiv.auth.controller;

import com.fanfixiv.auth.dto.BaseResultDto;
import com.fanfixiv.auth.dto.register.CertEmailDto;
import com.fanfixiv.auth.dto.reset.ResetTokenDto;
import com.fanfixiv.auth.service.ResetService;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/reset")
public class ResetController {

  private final ResetService resetService;

  @PostMapping("/email")
  public BaseResultDto resetEmail(@RequestBody @Valid CertEmailDto dto) {
    return resetService.resetEmail(dto);
  }

  @PostMapping("/pw")
  public BaseResultDto resetPw(@RequestBody @Valid ResetTokenDto dto) {
    return resetService.resetPw(dto);
  }

}
