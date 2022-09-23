package com.fanfixiv.auth.dto.register;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoubleCheckDto {
  private boolean can_use;
}
