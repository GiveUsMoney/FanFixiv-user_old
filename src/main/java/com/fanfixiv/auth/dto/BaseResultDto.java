package com.fanfixiv.auth.dto;

import lombok.Getter;

@Getter
public class BaseResultDto {
  private int status;

  public BaseResultDto() {
    this.status = 200;
  }
}
