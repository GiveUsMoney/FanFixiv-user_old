package com.fanfixiv.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@Profile({"!prod"})
@EnableSwagger2
public class SwaggerConfig {

  private final String API_TITLE = "Fanfixiv 인증 RestAPI";
  private final String API_VERSION = "1.0";
  private final String API_DESCRIPTION = "Fanfixiv 인증 ReactApi Swagger입니다.";
  
  @Bean
  public Docket restAPI() {
      return new Docket(DocumentationType.SWAGGER_2)
              .apiInfo(apiInfo())
              .select()
              .apis(RequestHandlerSelectors.basePackage("com.fanfixiv.auth"))
              .paths(PathSelectors.any())
              .build();
  }

  private ApiInfo apiInfo() {
      return new ApiInfoBuilder()
              .title(API_TITLE)
              .version(API_VERSION)
              .description(API_DESCRIPTION)
              .build();
  }
}