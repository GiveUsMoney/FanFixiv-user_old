package com.fanfixiv.auth.controller.advice;

import com.fanfixiv.auth.controller.LoginController;
import com.fanfixiv.auth.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = {LoginController.class})
public class LoginControllerAdvice {
  @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
  public ResponseEntity<ErrorResponse> handleLoginException(Exception e) {
    ErrorResponse err = new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    return new ResponseEntity<ErrorResponse>(err, HttpStatus.UNAUTHORIZED);
  }
}
