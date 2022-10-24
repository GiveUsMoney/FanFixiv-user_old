package com.fanfixiv.auth.filter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;

public class CustomTokenResponseConverter implements
    Converter<Map<String, Object>, OAuth2AccessTokenResponse> {

  @Override
  public OAuth2AccessTokenResponse convert(Map<String, Object> tokenResponseParameters) {
    String accessToken = (String) tokenResponseParameters.get(OAuth2ParameterNames.ACCESS_TOKEN);
    Long expire = Long.valueOf(String.valueOf(tokenResponseParameters.get(OAuth2ParameterNames.EXPIRES_IN)));

    Set<String> scopes = Collections.emptySet();
    if (tokenResponseParameters.containsKey(OAuth2ParameterNames.SCOPE)) {
      String scope = (String) tokenResponseParameters.get(OAuth2ParameterNames.SCOPE);
      scopes = Arrays.stream(StringUtils.delimitedListToStringArray(scope, ","))
          .collect(Collectors.toSet());
    }

    return OAuth2AccessTokenResponse.withToken(accessToken)
        .tokenType(TokenType.BEARER)
        .expiresIn(expire)
        .scopes(scopes)
        .build();
  }

}
