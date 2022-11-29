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
  private String profileImgW100;
  private String descript;
  private boolean isTr;
  private boolean isSocial;
  private boolean isNotice;

  public ProfileResultDto(UserEntity user) {
    this.email = user.getEmail();
    this.nickname = user.getProfile().getNickname();
    this.nnMdDate = user.getProfile().getNnMdDate();
    this.birth = user.getProfile().getBirth();
    this.profileImg = user.getProfile().getProfileImg();
    this.profileImgW100 = (user.isSocial() && this.profileImg != null)
        ? this.profileImg
        : this.profileImg.replace("/origin/", "/w_100/");
    this.descript = user.getProfile().getDescript();
    this.isTr = user.getProfile().isTr();
    this.isSocial = user.isSocial();
    this.isNotice = user.getNotice().stream().filter(x -> !x.isChecked()).toList().size() > 0;
  }

}
