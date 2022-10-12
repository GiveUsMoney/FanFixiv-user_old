package com.fanfixiv.auth.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomRequestEntityConverter implements
    Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

  private OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;
  private ObjectMapper objectMapper;

  public CustomRequestEntityConverter() {
    defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
    objectMapper = new ObjectMapper();
  }

  @Override
  public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
    RequestEntity<?> entity = defaultConverter.convert(req);

    Map<String, List<String>> body = objectMapper.convertValue(entity.getBody(), Map.class);

    body.put("code_verifier", new ArrayList<String>() {
      {
        add("challenge");
      }
    });

    return new RequestEntity<>(body, entity.getHeaders(), entity.getMethod(), entity.getUrl());
  }

}