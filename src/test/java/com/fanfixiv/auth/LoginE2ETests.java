package com.fanfixiv.auth;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fanfixiv.auth.dto.login.LoginDto;
import com.fanfixiv.auth.dto.profile.ProfileResultDto;
import com.fanfixiv.auth.entity.ProfileEntity;
import com.fanfixiv.auth.entity.UserEntity;
import com.fanfixiv.auth.repository.jpa.UserRepository;
import com.fanfixiv.auth.repository.redis.RedisLoginRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginE2ETests {

  @Autowired
  UserRepository userRepository;

  @Autowired
  RedisLoginRepository redisLoginRepository;

  @Autowired
  BCryptPasswordEncoder passwordEncoder;

  @LocalServerPort
  private int _port;

  @BeforeEach
  public void setUp() throws Exception {
    port = _port;
  }

  ObjectMapper objectMapper = new ObjectMapper();

  static String accessToken = "";
  static String refreshToken = "";

  static UserEntity user;

  private String objToJson(Object obj) {
    try {
      return this.objectMapper.writeValueAsString(obj);
    } catch (Exception e) {
      return "";
    }
  }

  @Test
  @Order(1)
  @DisplayName("POST /login 200")
  void doLogin_e2e_200() {

    String email = "test@example.com";
    String pw = "password";
    String nick = "테스트계정";

    ProfileEntity profile = ProfileEntity.builder()
        .nickname(nick)
        .birth(LocalDate.of(2002, 8, 19))
        .build();

    LoginE2ETests.user = UserEntity.builder()
        .email(email)
        .pw(pw)
        .profile(profile)
        .build();

    user.hashPassword(passwordEncoder);

    userRepository.save(user);

    LoginDto lgdto = new LoginDto(email, pw);

    redisLoginRepository.deleteById(email);

    ExtractableResponse<Response> res = given()
        .contentType("application/json")
        .body(this.objToJson(lgdto))
        .post("/login")
        .then()
        .statusCode(200)
        .extract();

    LoginE2ETests.refreshToken = res.header("Set-Cookie").replace("refreshToken=", "");
    LoginE2ETests.accessToken = res.path("token");
  }

  @Test
  @Order(1)
  @DisplayName("POST /login 401")
  void doLogin_e2e_401() {

    String email = "test@example.com";
    String pw = "not_password";

    LoginDto lgdto = new LoginDto(email, pw);

    for (int i = 0; i < 5; i++) {
      given()
          .contentType("application/json")
          .body(this.objToJson(lgdto))
          .post("/login")
          .then()
          .statusCode(401);
    }

    given()
        .contentType("application/json")
        .body(this.objToJson(lgdto))
        .post("/login")
        .then()
        .statusCode(401)
        .assertThat()
        .body("message", equalTo("계속된 로그인 실패로 30분간 로그인이 불가능합니다."));
  }

  @Test
  @Order(1)
  @DisplayName("POST /login 400")
  void doLogin_e2e_400() {

    String email = "test@example.com";

    LoginDto lgdto = new LoginDto(email, null);

    given()
        .contentType("application/json")
        .body(this.objToJson(lgdto))
        .post("/login")
        .then()
        .statusCode(400);

  }

  @Test
  @Order(2)
  @DisplayName("GET /profile 200")
  void getUserProfile_e2e_200() {
    ProfileResultDto actual = new ProfileResultDto(LoginE2ETests.user);
    given()
        .contentType("application/json")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + LoginE2ETests.accessToken)
        .get("/profile")
        .then()
        .statusCode(200)
        .assertThat()
        .body("email", equalTo(actual.getEmail()))
        .body("nickname", equalTo(actual.getNickname()))
        .body("birth", equalTo(actual.getBirth().toString()))
        .body("descript", equalTo(actual.getDescript()));
  }

  @Test
  @Order(2)
  @DisplayName("GET /profile 401")
  void getUserProfile_e2e_401() {
    given()
        .contentType("application/json")
        .post("/profile")
        .then()
        .statusCode(401);
  }

  @Test
  @Order(3)
  @DisplayName("POST /refresh 200")
  void refreshToken_e2e_200() {
    ExtractableResponse<Response> res = given()
        .contentType("application/json")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + LoginE2ETests.accessToken)
        .cookie("refreshToken", LoginE2ETests.refreshToken)
        .post("/refresh")
        .then()
        .statusCode(200)
        .extract();

    LoginE2ETests.accessToken = res.path("token");
  }

  @Test
  @Order(3)
  @DisplayName("POST /refresh 401")
  void refreshToken_e2e_401() {
    given()
        .contentType("application/json")
        .header(HttpHeaders.AUTHORIZATION, "Bearer TESTTOKEN")
        .cookie("refreshToken", "TESTTOKEN")
        .post("/refresh")
        .then()
        .statusCode(401);
  }

  @Test
  @Order(4)
  @DisplayName("POST /logout 200")
  void logout_e2e_200() {
    given()
        .contentType("application/json")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + LoginE2ETests.accessToken)
        .cookie("refreshToken", LoginE2ETests.refreshToken)
        .post("/logout")
        .then()
        .statusCode(200)
        .assertThat()
        .body("success", equalTo(true));
  }

  @Test
  @Order(4)
  @DisplayName("POST /logout 401")
  void logout_e2e_401() {
    given()
        .contentType("application/json")
        .header(HttpHeaders.AUTHORIZATION, "Bearer TESTTOKEN")
        .cookie("refreshToken", "TESTTOKEN")
        .post("/logout")
        .then()
        .statusCode(401);
  }

}
