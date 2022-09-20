package com.fanfixiv.auth.controller.advice;

import com.fanfixiv.auth.controller.LoginController;
import com.fanfixiv.auth.exception.ErrorResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = {LoginController.class})
public class LoginControllerAdvice {
  @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
  public ResponseEntity<ErrorResponse> handleLoginException(Exception e) {
    ErrorResponse err = new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    return new ResponseEntity<ErrorResponse>(err, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException e) {
    List<String> errLst =
        e.getBindingResult().getAllErrors().stream()
            .map((error) -> ((FieldError) error).getField() + "이(가) " + error.getDefaultMessage())
            .toList();
    ErrorResponse err = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), errLst);
    return new ResponseEntity<ErrorResponse>(err, HttpStatus.BAD_REQUEST);
  }
}
