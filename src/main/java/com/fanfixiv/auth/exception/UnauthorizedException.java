package com.fanfixiv.auth.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseStatusException {
  public UnauthorizedException(String message) {
    super(HttpStatus.UNAUTHORIZED, message);
  }
}
