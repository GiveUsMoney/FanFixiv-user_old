package com.fanfixiv.auth.dto.server;

import lombok.Getter;

@Getter
public class BaseResultFormDto {
  private int status;
  private String message;
}
