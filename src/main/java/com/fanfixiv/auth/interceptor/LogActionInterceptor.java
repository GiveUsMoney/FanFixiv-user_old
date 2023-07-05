package com.fanfixiv.auth.interceptor;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fanfixiv.auth.details.User;
import com.fanfixiv.auth.dto.action.ActionDto;
import com.fanfixiv.auth.requester.MQRequester;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LogActionInterceptor implements HandlerInterceptor {

  private final MQRequester mqRequester;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();

    Long seq = -1L;

    if (user != null) {
      User u = (User) user.getPrincipal();
      seq = u.getUserSeq();
    }

    Object data = (HttpMethod.POST.matches(request.getMethod()) ||
        HttpMethod.PUT.matches(request.getMethod()))
            ? null // Body를 꺼내올 방법을 못찾겠다...
            : request.getParameterMap();

    ActionDto action = new ActionDto(
        request.getRemoteAddr(),
        seq,
        request.getRequestURI(),
        data,
        LocalDateTime.now().toString());

    mqRequester.sendAction(action);

    return true;
  }
}
