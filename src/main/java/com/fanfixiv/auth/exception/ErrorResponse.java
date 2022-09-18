package com.fanfixiv.auth.exception;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse {
  private int status;
  private String message;
  private List<String> err;

  public ErrorResponse(HttpStatus status, String message) {
    this.status = status.value();
    this.message = message;
  }

  public ErrorResponse(HttpStatus status, String message, List<String> err) {
    this.status = status.value();
    this.message = message;
    this.err = err;
  }
}
