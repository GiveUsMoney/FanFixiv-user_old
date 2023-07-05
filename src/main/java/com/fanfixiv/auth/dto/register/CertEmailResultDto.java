package com.fanfixiv.auth.dto.register;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CertEmailResultDto {
  private String uuid;
  private LocalDateTime endTime;
}
