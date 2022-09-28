package com.fanfixiv.auth.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "tb_profile")
public class ProfileEntity extends BaseEntity {
  @Column
  private String nickname;

  @Column
  @ColumnDefault("now()")
  private LocalDateTime nn_md_date;

  @Column
  private LocalDate birth;

  @Column
  private String profile_img;

  @Column
  private String descript;

  @Column
  @ColumnDefault("false")
  private boolean is_tr;
}
