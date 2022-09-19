package com.fanfixiv.auth.controller.advice;

import com.fanfixiv.auth.controller.RegisterController;
import com.fanfixiv.auth.exception.EmailDuplicateException;
import com.fanfixiv.auth.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = {RegisterController.class})
public class RegisterControllerAdvice {
  @ExceptionHandler({MissingServletRequestParameterException.class, EmailDuplicateException.class})
  public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(Exception e) {
    ErrorResponse err = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    return new ResponseEntity<ErrorResponse>(err, HttpStatus.BAD_REQUEST);
  }
}
