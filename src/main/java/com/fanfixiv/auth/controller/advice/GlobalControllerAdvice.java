package com.fanfixiv.auth.controller.advice;

import com.fanfixiv.auth.controller.LoginController;
import com.fanfixiv.auth.controller.RegisterController;
import com.fanfixiv.auth.exception.DuplicateException;
import com.fanfixiv.auth.exception.ErrorResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = { LoginController.class, RegisterController.class })
public class GlobalControllerAdvice {
  @ExceptionHandler({ UsernameNotFoundException.class, BadCredentialsException.class })
  public ResponseEntity<ErrorResponse> handleLoginException(Exception e) {
    ErrorResponse err = new ErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    return new ResponseEntity<ErrorResponse>(err, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({ MissingServletRequestParameterException.class, DuplicateException.class })
  public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(Exception e) {
    ErrorResponse err = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    return new ResponseEntity<ErrorResponse>(err, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ MethodArgumentNotValidException.class })
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException e) {
    List<String> errLst = e.getBindingResult().getAllErrors().stream()
        .map((error) -> ((FieldError) error).getField() + "이(가) " + error.getDefaultMessage())
        .toList();
    ErrorResponse err = new ErrorResponse(HttpStatus.BAD_REQUEST, "입력된 값이 올바르지 않습니다.", errLst);
    return new ResponseEntity<ErrorResponse>(err, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ BindException.class })
  public ResponseEntity<ErrorResponse> handleValidationExceptions(BindException e) {
    List<String> errLst = e.getBindingResult().getAllErrors().stream()
        .map((error) -> ((FieldError) error).getField() + "이(가) " + error.getDefaultMessage())
        .toList();
    ErrorResponse err = new ErrorResponse(HttpStatus.BAD_REQUEST, "입력된 값이 올바르지 않습니다.", errLst);
    return new ResponseEntity<ErrorResponse>(err, HttpStatus.BAD_REQUEST);
  }
}
