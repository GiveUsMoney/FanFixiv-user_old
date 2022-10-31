package com.fanfixiv.auth.dto.profile;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fanfixiv.auth.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResultDto {
  private String email;
  private String nickname;
  private LocalDateTime nnMdDate;
  private LocalDate birth;
  private String profileImg;
  private String descript;
  private boolean isTr;

  public ProfileResultDto(UserEntity user) {
    this.email = user.getEmail();
    this.nickname = user.getProfile().getNickname();
    this.nnMdDate = user.getProfile().getNnMdDate();
    this.birth = user.getProfile().getBirth();
    this.profileImg = user.getProfile().getProfileImg();
    this.descript = user.getProfile().getDescript();
    this.isTr = user.getProfile().isTr();
  }

}
