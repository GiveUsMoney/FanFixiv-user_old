package com.fanfixiv.auth.exception;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import com.fanfixiv.auth.dto.server.BaseResultFormDto;
import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
  private int status;
  private String message;
  private List<String> err;
  private BaseResultFormDto data;

  public ErrorResponse(HttpStatus status, String message) {
    this.status = status.value();
    this.message = message;
  }

  public ErrorResponse(HttpStatus status, String message, List<String> err) {
    this.status = status.value();
    this.message = message;
    this.err = err;
  }

  public ErrorResponse(HttpStatus status, String message, BaseResultFormDto data) {
    this.status = status.value();
    this.message = message;
    this.data = data;
  }
}
