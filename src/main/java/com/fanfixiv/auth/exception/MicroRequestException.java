package com.fanfixiv.auth.exception;

import java.util.List;

import lombok.Getter;

@Getter
public class MicroRequestException extends RuntimeException {

  private List<String> defaultMessage;

  public MicroRequestException(String msg, List<String> defaultMessage) {
    super(msg);
    this.defaultMessage = defaultMessage;
  }
}
