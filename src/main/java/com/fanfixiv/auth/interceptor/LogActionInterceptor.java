package com.fanfixiv.auth.interceptor;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fanfixiv.auth.dto.action.ActionDto;
import com.fanfixiv.auth.requester.MQRequester;
import com.fanfixiv.auth.utils.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LogActionInterceptor implements HandlerInterceptor {

  private final MQRequester mqRequester;

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    String token = request.getHeader(HttpHeaders.AUTHORIZATION);

    Long user = Long.parseLong(jwtTokenProvider.getUserPk(token));

    Object data = HttpMethod.POST.matches(request.getMethod())
        ? request.getReader().lines().collect(Collectors.joining(System.lineSeparator()))
        : request.getParameterMap();

    ActionDto action = new ActionDto(
        request.getRemoteAddr(),
        user,
        request.getRequestURI(),
        data,
        LocalDateTime.now().toString());

    mqRequester.sendAction(action);

    return true;
  }
}
