package com.fanfixiv.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_notice")
public class NoticeEntity extends BaseEntity {
  @Column
  private String content;

  @Column
  private boolean toAll;

  @Column
  @ColumnDefault(value = "false")
  private boolean checked;

  @ManyToOne
  @JoinColumn(name = "user_seq")
  private UserEntity user;
}
