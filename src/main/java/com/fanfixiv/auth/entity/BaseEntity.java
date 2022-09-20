package com.fanfixiv.auth.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Builder
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseEntity {

  @Id @GeneratedValue protected long seq;

  @CreatedDate
  @Column(updatable = false, nullable = false)
  protected LocalDateTime createdAt;

  @LastModifiedDate protected LocalDateTime updatedAt;
}
