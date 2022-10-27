package com.fanfixiv.auth.exception;

public class PasswordNotCorrectException extends RuntimeException {
  public PasswordNotCorrectException(String msg) {
    super(msg);
  }
}
