package com.fanfixiv.auth.dto.action;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ActionDto {
  private String ip;
  private Long user;
  private String path;
  private Object data;
  private String time;
}
