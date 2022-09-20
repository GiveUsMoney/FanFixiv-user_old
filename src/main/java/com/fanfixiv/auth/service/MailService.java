package com.fanfixiv.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.fanfixiv.auth.dto.email.EmailDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailService {

  @Autowired
  AmazonSimpleEmailService amazonSimpleEmailService;

  public void sendMail(final String subject, final String content, final List<String> receivers) {
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
