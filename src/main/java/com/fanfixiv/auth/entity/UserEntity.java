package com.fanfixiv.auth.entity;

import com.fanfixiv.auth.interfaces.UserRoleEnum;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "tb_user")
public class UserEntity extends BaseEntity {

  @Column private String id;

  @Column private String pw;

  @Enumerated(EnumType.STRING)
  private UserRoleEnum role = UserRoleEnum.USER;
}
