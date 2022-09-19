package com.fanfixiv.auth.exception;

public class EmailDuplicateException extends RuntimeException {
  public EmailDuplicateException(String msg) {
    super(msg);
  }
}
