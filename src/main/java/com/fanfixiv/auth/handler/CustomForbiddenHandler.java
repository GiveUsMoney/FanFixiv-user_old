package com.fanfixiv.auth.handler;

import com.fanfixiv.auth.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomForbiddenHandler implements AccessDeniedHandler {

  private ObjectMapper mapper = new ObjectMapper();

  @Override
  public void handle(HttpServletRequest req, HttpServletResponse res,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {

    res.setContentType("application/json;charset=UTF-8");
    res.setStatus(403);
    res.getWriter()
        .write(
            mapper.writeValueAsString(
                new ErrorResponse(HttpStatus.FORBIDDEN, "잘못된 접근입니다.")));

  }
}
