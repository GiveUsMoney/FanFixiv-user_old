package com.fanfixiv.auth.dto.register;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DoubleCheckDto {
  private boolean canUse;
}
