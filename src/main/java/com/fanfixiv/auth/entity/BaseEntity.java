package com.fanfixiv.auth.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected long seq;

  @CreatedDate
  @ColumnDefault("now()")
  @Column(updatable = false, nullable = false) 
  protected LocalDateTime createdAt;

  @LastModifiedDate
  @ColumnDefault("now()")
  @Column(nullable = false)
  protected LocalDateTime updatedAt;
}
