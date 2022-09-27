package com.fanfixiv.auth.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

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

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "user_seq")
  private List<RoleEntity> role;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "profile_seq")
  private ProfileEntity profile;

  /**
   * 비밀번호를 암호화
   * @param passwordEncoder 암호화 할 인코더 클래스
   * @return 변경된 유저 Entity
   */
  public UserEntity hashPassword(PasswordEncoder passwordEncoder) {
    this.pw = passwordEncoder.encode(this.pw);
    return this;
  }

  /**
   * 비밀번호 확인
   * @param plainPassword 암호화 이전의 비밀번호
   * @param passwordEncoder 암호화에 사용된 클래스
   * @return true | false
   */
  public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder) {
    return passwordEncoder.matches(plainPassword, this.pw);
  }
}
