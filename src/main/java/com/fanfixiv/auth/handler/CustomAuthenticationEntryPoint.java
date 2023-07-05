package com.fanfixiv.auth.handler;

import com.fanfixiv.auth.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private ObjectMapper mapper = new ObjectMapper();

  @Override
  public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex)
      throws IOException, ServletException {

    res.setContentType("application/json;charset=UTF-8");
    res.setStatus(401);
    res.getWriter()
        .write(
            mapper.writeValueAsString(
                new ErrorResponse(HttpStatus.UNAUTHORIZED, "로그인이 되어있지 않습니다.")));
  }
}
