package com.fanfixiv.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TempEntity {
  private int id;
  private String content;
}
