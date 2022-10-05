package com.fanfixiv.auth.dto.profile;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fanfixiv.auth.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResultDto {
  private String email;
  private String nickname;
  private LocalDateTime nn_md_date;
  private LocalDate birth;
  private String profile_img;
  private String descript;
  private boolean is_tr;

  public ProfileResultDto(UserEntity user) {
    this.email = user.getEmail();
    this.nickname = user.getProfile().getNickname();
    this.nn_md_date = user.getProfile().getNn_md_date();
    this.birth = user.getProfile().getBirth();
    this.profile_img = user.getProfile().getProfile_img();
    this.descript = user.getProfile().getDescript();
    this.is_tr = user.getProfile().is_tr();
  }

}
