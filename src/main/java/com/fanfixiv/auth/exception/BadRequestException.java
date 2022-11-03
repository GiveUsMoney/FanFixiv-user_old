package com.fanfixiv.auth.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseStatusException {
  public BadRequestException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
