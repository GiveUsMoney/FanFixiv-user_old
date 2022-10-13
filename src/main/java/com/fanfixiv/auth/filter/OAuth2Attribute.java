package com.fanfixiv.auth.filter;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuth2Attribute {
  private Map<String, String> attributes;
  private String attributeKey;
  private String id;
  private String name;
  private String username;
  private String profileImageUrl;
  private String description;

  @SuppressWarnings("unchecked")
  public static OAuth2Attribute of(String attributeKey, Map<String, Object> attributes) {
    Map<String, String> attr;
    attr = (Map<String, String>) attributes.get("data");
    return OAuth2Attribute.builder()
        .id(attr.get("id"))
        .name(attr.get("name"))
        .username(attr.get("username"))
        .profileImageUrl(attr.get("profile_image_url"))
        .description(attr.get("description"))
        .attributes(attr)
        .attributeKey(attributeKey)
        .build();
  }

  public Map<String, Object> convertToMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("id", id);
    map.put("name", name);
    map.put("username", username);
    map.put("profile_image_url", profileImageUrl);
    map.put("description", description);

    return map;
  }
}