package com.fanfixiv.auth.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_secession")
public class SecessionEntity extends BaseEntity {

  @Column
  private String email;

  @Column
  private LocalDate secDate;

  public SecessionEntity(String email) {
    this.email = email;
    this.secDate = LocalDate.now();
  }

}
