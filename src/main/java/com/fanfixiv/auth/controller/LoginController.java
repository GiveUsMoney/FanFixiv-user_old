package com.fanfixiv.auth.controller;

import com.fanfixiv.auth.details.User;
import com.fanfixiv.auth.dto.login.LoginDto;
import com.fanfixiv.auth.dto.login.LoginResultDto;
import com.fanfixiv.auth.dto.profile.ProfileResultDto;
import com.fanfixiv.auth.service.LoginService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

  @PostMapping("/refresh")
  public LoginResultDto refresh(
      @CookieValue("refreshToken") String refresh, @RequestHeader("Authorization") String token)
      throws Exception {
    return loginService.refershToken(refresh, token);
  }

  @GetMapping("/profile")
  public ProfileResultDto profile(@AuthenticationPrincipal User user)
      throws Exception {
    return new ProfileResultDto(user.getUser());
  }
}
