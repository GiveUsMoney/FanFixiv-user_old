package com.fanfixiv.auth.controller.advice;

import com.fanfixiv.auth.controller.LoginController;
import com.fanfixiv.auth.controller.NoticeController;
import com.fanfixiv.auth.controller.RegisterController;
import com.fanfixiv.auth.controller.ResetController;
import com.fanfixiv.auth.exception.BadRequestException;
import com.fanfixiv.auth.exception.BaseStatusException;
import com.fanfixiv.auth.exception.ErrorResponse;
import com.fanfixiv.auth.exception.MicroRequestException;
import com.fanfixiv.auth.exception.UnauthorizedException;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice(basePackageClasses = {
    LoginController.class,
    RegisterController.class,
    ResetController.class,
    NoticeController.class
})
public class GlobalControllerAdvice {

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.initDirectFieldAccess();
  }

  @ExceptionHandler({ UnauthorizedException.class, BadRequestException.class })
  public ResponseEntity<ErrorResponse> handleLoginException(BaseStatusException e) {
    ErrorResponse err = new ErrorResponse(e.getStatus(), e.getMessage());
    return new ResponseEntity<ErrorResponse>(err, e.getStatus());
  }

  @ExceptionHandler({
      MicroRequestException.class
  })
  public ResponseEntity<ErrorResponse> handleMissingMicroRequestException(MicroRequestException e) {
    ErrorResponse err = e.getData() == null
        ? new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), e.getData())
        : new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), e.getErr());
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
