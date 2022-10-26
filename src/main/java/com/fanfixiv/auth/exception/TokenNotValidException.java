package com.fanfixiv.auth.exception;

public class TokenNotValidException extends RuntimeException {
  public TokenNotValidException(String msg) {
    super(msg);
  }
}
