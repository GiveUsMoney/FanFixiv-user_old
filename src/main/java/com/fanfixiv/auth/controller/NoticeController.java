package com.fanfixiv.auth.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fanfixiv.auth.details.User;
import com.fanfixiv.auth.dto.BaseResultDto;
import com.fanfixiv.auth.dto.notice.NoticeResultDto;
import com.fanfixiv.auth.service.NoticeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

  private final NoticeService noticeService;

  @GetMapping("")
  public List<NoticeResultDto> alermNotice(@AuthenticationPrincipal User user) throws Exception {
    return this.noticeService.findNotice(user);
  }

  @GetMapping("personal")
  public List<NoticeResultDto> alermPersonal(@AuthenticationPrincipal User user) throws Exception {
    return this.noticeService.findPersonal(user);
  }

  @PutMapping("personal")
  public BaseResultDto checkPersonal(@AuthenticationPrincipal User user, @RequestParam Long seq)
      throws Exception {
    return this.noticeService.checkPersonal(user, seq);
  }
}
