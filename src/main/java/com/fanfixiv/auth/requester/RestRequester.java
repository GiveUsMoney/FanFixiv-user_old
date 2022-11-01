package com.fanfixiv.auth.requester;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fanfixiv.auth.dto.server.BaseResultFormDto;
import com.fanfixiv.auth.exception.MicroRequestException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RestRequester {

  @Value("${aws.s3.url}")
  private String s3Url;

  private final RestTemplate restTemplate;

  private <T extends BaseResultFormDto, E> T postRequest(String uri, E dto, Class<T> clazz) {
    T result;

    try {
      result = restTemplate.postForObject(
          uri,
          dto,
          clazz);
    } catch (RestClientException e) {
      throw new MicroRequestException(
          "메인서버의 응답이 올바르지 않습니다.",
          Arrays.asList(e.getMessage()));
    }

    if (result == null || result.getStatus() == 400)
      throw new MicroRequestException(
          "메인서버의 응답이 올바르지 않습니다.",
          result == null ? null : Arrays.asList(result.getMessage()));

    return result;
  }

}
