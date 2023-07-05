package com.fanfixiv.auth.exception;

import java.util.List;

import com.fanfixiv.auth.dto.server.BaseResultFormDto;

import lombok.Getter;

@Getter
public class MicroRequestException extends RuntimeException {

  private BaseResultFormDto data;
  private List<String> err;

  public MicroRequestException(String msg, BaseResultFormDto data) {
    super(msg);
    this.data = data;
  }

  public MicroRequestException(String msg, List<String> err) {
    super(msg);
    this.err = err;
  }
}
