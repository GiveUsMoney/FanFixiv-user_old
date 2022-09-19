package com.fanfixiv.auth.dto.register;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertEmailResultDto {
  private String uuid;
  private LocalDateTime endTime;
}
