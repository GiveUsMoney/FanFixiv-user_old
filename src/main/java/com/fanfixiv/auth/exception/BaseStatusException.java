package com.fanfixiv.auth.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BaseStatusException extends RuntimeException {
  protected HttpStatus status;

  public BaseStatusException(HttpStatus status, String message) {
    super(message);
    this.status = status;
  }

}
