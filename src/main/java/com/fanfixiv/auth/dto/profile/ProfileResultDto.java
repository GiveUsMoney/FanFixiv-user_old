package com.fanfixiv.auth.dto.profile;

import com.fanfixiv.auth.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResultDto {
  // TODO: 후일 프로필 DB가 추가되면 더 많은 property를 포함할 예정
  private String email;

  public ProfileResultDto(UserEntity user) {
    this.email = user.getEmail();
  }

}
