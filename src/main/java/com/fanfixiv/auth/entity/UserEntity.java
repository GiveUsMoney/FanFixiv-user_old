package com.fanfixiv.auth.entity;

import com.fanfixiv.auth.interfaces.UserRoleEnum;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Builder
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_user")
public class UserEntity extends BaseEntity {

  @Column private String email;

  @Column private String pw;

  @Column private String salt;

  @Enumerated(EnumType.STRING)
  @ColumnDefault(value = "'USER'")
  private UserRoleEnum role;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "profile_seq")
  private ProfileEntity profile;
}
