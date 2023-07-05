package com.fanfixiv.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.fanfixiv.auth.dto.email.EmailDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

  @Value("${micro.frontend.url}")
  private String origin;

  private final AmazonSimpleEmailService amazonSimpleEmailService;

  private final SpringTemplateEngine templateEngine;

  public void sendEmailAuthMail(final String number, final List<String> receivers) {
    Context context = new Context();

    context.setVariable("number", number);

    String html = templateEngine.process("auth", context);

    sendMail("FanFixiv 본인인증 이메일", html, receivers);
  }

  public void sendResetPwMail(final String resetUrl, final List<String> receivers) {
    Context context = new Context();

    // TODO: 후일 비밀번호 초기화 페이지 프론트엔드 개발되면 경로를 바꿀 것.
    context.setVariable("resetUrl", origin + "reset/pw/" + resetUrl);

    String html = templateEngine.process("reset", context);

    sendMail("FanFixiv 비밀번호 초기화 이메일", html, receivers);
  }

  private void sendMail(final String subject, final String content, final List<String> receivers) {
    final EmailDto senderDto = EmailDto.builder() // 1
        .to(receivers)
        .subject(subject)
        .content(content)
        .build();

    final SendEmailResult sendEmailResult = amazonSimpleEmailService // 2
        .sendEmail(senderDto.toSendRequestDto());

    sendingResultMustSuccess(sendEmailResult); // 3
  }

  private void sendingResultMustSuccess(final SendEmailResult sendEmailResult) {
    if (sendEmailResult.getSdkHttpMetadata().getHttpStatusCode() != 200) {
      log.error("{}", sendEmailResult.getSdkResponseMetadata().toString());
    }
  }
}
