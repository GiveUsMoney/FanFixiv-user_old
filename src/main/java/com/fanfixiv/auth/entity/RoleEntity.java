package com.fanfixiv.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fanfixiv.auth.interfaces.UserRoleEnum;

@Getter
@Setter
@Builder
@Entity
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@Table(name = "tb_role")
public class RoleEntity extends BaseEntity {

  @Column(name = "user_seq")
  private Long userSeq;

  @Enumerated(EnumType.STRING)
  @ColumnDefault(value = "'ROLE_USER'")
  private UserRoleEnum role;

  public RoleEntity() {
    this.role = UserRoleEnum.ROLE_USER;
  }

  public RoleEntity(UserRoleEnum role) {
    this.role = role;
  }
}
