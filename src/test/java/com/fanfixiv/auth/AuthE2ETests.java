package com.fanfixiv.auth;

import static io.restassured.RestAssured.*;
// import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthE2ETests {

  @LocalServerPort
  private int _port;

  @BeforeEach
  public void setUp() throws Exception {
    port = _port;
  }

  @Test
  @DisplayName("임시 API e2e 테스트")
  void tempApiBasic_e2e() {
    get("/")
        .then()
        .statusCode(200)
        .assertThat()
        .body("content", equalTo("Hello World!!!"))
        .body("id", equalTo(0));
  }
}
