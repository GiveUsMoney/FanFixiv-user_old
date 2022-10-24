package com.fanfixiv.auth.filter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.util.MultiValueMap;

public class CustomRequestEntityConverter implements
    Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

  private OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;

  public CustomRequestEntityConverter() {
    defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
  }

  @Override
  @SuppressWarnings({ "null", "unchecked" })
  public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
    RequestEntity<?> entity = defaultConverter.convert(req);

    MultiValueMap<String, String> body = (MultiValueMap<String, String>) entity.getBody();
    body.add("code_verifier", "challenge");

    return new RequestEntity<>(body, entity.getHeaders(), entity.getMethod(), entity.getUrl());
  }

}