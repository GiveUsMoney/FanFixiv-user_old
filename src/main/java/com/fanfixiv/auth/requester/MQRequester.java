package com.fanfixiv.auth.requester;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.fanfixiv.auth.dto.server.ProfileFormDto;
import com.fanfixiv.auth.dto.server.ProfileFormResultDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MQRequester {

  @Value("${aws.s3.url}")
  private String s3Url;

  private final RabbitTemplate rabbitTemplate;

  public String profileImgForm(String key) {
    ProfileFormResultDto result = rabbitTemplate.<ProfileFormResultDto>convertSendAndReceiveAsType(
        "profile-img.form",
        new ProfileFormDto(key),
        new ParameterizedTypeReference<ProfileFormResultDto>() {
        });

    if (result == null) {
      return null;
    }

    if (result.getStatus() != 200) {
      return null;
    }

    return s3Url + result.getKey();
  }

}
