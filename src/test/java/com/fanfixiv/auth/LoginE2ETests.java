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
import com.fanfixiv.auth.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginE2ETests {

  @Autowired
  UserRepository userRepository;

  @Autowired
  BCryptPasswordEncoder passwordEncoder;

  @LocalServerPort
  private int _port;

  @BeforeEach
  public void setUp() throws Exception {
    port = _port;
  }

  ObjectMapper objectMapper = new ObjectMapper();

  static String access_token = "";
  static String refresh_token = "";

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

    LoginDto lgdto = new LoginDto();

    lgdto.setId(email);
    lgdto.setPw(pw);

    ExtractableResponse<Response> res = given()
        .contentType("application/json")
        .body(this.objToJson(lgdto))
        .post("/login")
        .then()
        .statusCode(200)
        .extract();

    LoginE2ETests.refresh_token = res.header("Set-Cookie");
    LoginE2ETests.access_token = res.path("token");
  }

  @Test
  @Order(1)
  @DisplayName("POST /login 401")
  void doLogin_e2e_401() {

    String email = "test@example.com";
    String pw = "not_password";

    LoginDto lgdto = new LoginDto();

    lgdto.setId(email);
    lgdto.setPw(pw);

    given()
        .contentType("application/json")
        .body(this.objToJson(lgdto))
        .post("/login")
        .then()
        .statusCode(401);
  }

  @Test
  @Order(1)
  @DisplayName("POST /login 400")
  void doLogin_e2e_400() {

    String email = "test@example.com";

    LoginDto lgdto = new LoginDto();

    lgdto.setId(email);

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
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + LoginE2ETests.access_token)
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

}
