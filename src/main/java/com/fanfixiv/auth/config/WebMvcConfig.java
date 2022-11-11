package com.fanfixiv.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fanfixiv.auth.interceptor.LogActionInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  private final LogActionInterceptor logActionInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(logActionInterceptor)
        .excludePathPatterns(
            "/",
            "/swagger-ui.html/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/roles/**");
  }

}
