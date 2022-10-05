package com.fanfixiv.auth.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile({"!prod"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {
  @GetMapping("/user")
  public String userRole() {
    return "해당 API는 USER 권한이 있는 사용자만 사용할수 있습니다.";
  }
  @GetMapping("/artist")
  public String artistRole() {
    return "해당 API는 ARTIST 권한이 있는 사용자만 사용할수 있습니다.";
  }
  @GetMapping("/trans")
  public String transRole() {
    return "해당 API는 TRANSLATER 권한이 있는 사용자만 사용할수 있습니다.";
  }
  @GetMapping("/admin")
  public String adminRole() {
    return "해당 API는 ADMIN 권한이 있는 사용자만 사용할수 있습니다.";
  }
}
