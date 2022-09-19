package com.fanfixiv.auth.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "tb_profile")
public class ProfileEntity extends BaseEntity {
  @Column private String nickname;

  @Column private LocalDateTime nn_md_date;

  @Column private String profile_img;

  @Column private String descript;

  @Column private boolean is_tr;

  @OneToOne(mappedBy = "profile")
  private UserEntity user;
}
