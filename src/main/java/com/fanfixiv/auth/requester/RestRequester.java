package com.fanfixiv.auth.requester;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fanfixiv.auth.dto.server.ProfileFormDto;
import com.fanfixiv.auth.dto.server.ProfileFormResultDto;
import com.fanfixiv.auth.exception.MicroRequestException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RestRequester {

  @Value("${micro.main-server.url}")
  private String mainServerUrl;

  @Value("${aws.s3.url}")
  private String s3Url;

  private final RestTemplate restTemplate;

  public String uploadProfileImg(String key) {
    String uri = mainServerUrl + "profile-img/form"; // or any other uri

    ProfileFormResultDto result = restTemplate.postForObject(
        uri,
        new ProfileFormDto(key),
        ProfileFormResultDto.class);

    if (result == null || result.getStatus() == 400)
      throw new MicroRequestException("메인서버의 응답이 올바르지 않습니다.");

    return s3Url + result.getKey();

  }

}
