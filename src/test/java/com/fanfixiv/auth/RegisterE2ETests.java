package com.fanfixiv.auth;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;

import com.fanfixiv.auth.dto.login.LoginDto;
import com.fanfixiv.auth.dto.register.RegisterDto;
import com.fanfixiv.auth.dto.secession.SecessionDto;
import com.fanfixiv.auth.entity.ProfileEntity;
import com.fanfixiv.auth.repository.ProfileRepository;
import com.fanfixiv.auth.service.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterE2ETests {

  @Autowired
  ProfileRepository profileRepository;

  @LocalServerPort
  private int _port;

  @BeforeEach
  public void setUp() throws Exception {
    port = _port;
  }

  @Test
  @Order(1)
  @DisplayName("GET /register/dc-nick 200")
  void isNickDouble_e2e_200() {
    given()
        .param("nickname", "test1")
        .get("/register/dc-nick")
        .then()
        .statusCode(200)
        .assertThat()
        .body("canUse", equalTo(true));

    profileRepository.save(
        ProfileEntity.builder()
            .nickname("test1")
            .build());

    given()
        .param("nickname", "test1")
        .get("/register/dc-nick")
        .then()
        .statusCode(200)
        .assertThat()
        .body("canUse", equalTo(false));

  }

  @Test
  @Order(1)
  @DisplayName("GET /register/dc-nick 400")
  void isNickDouble_e2e_400() {
    given()
        .get("/register/dc-nick")
        .then()
        .statusCode(400);
  }

  // ===

  @MockBean
  MailService mailService;

  static String uuid = "";
  static String num = "";

  @Test
  @Order(2)
  @DisplayName("GET /register/cert-email 200")
  void certEmail_e2e_200() {
    
    doAnswer(
      new Answer<Object>() {
      public Object answer(InvocationOnMock invocation) {
          RegisterE2ETests.num = (String)invocation.getArgument(0);
          return null;
      }})
    .when(mailService)
    .sendEmailAuthMail(anyString(), anyList());
    
    RegisterE2ETests.uuid = given()
        .param("email", "register@test.com")
        .get("/register/cert-email")
        .then()
        .statusCode(200)
        .extract()
        .path("uuid");
  }

  @Test
  @Order(2)
  @DisplayName("GET /register/cert-email 400")
  void certEmail_e2e_400() {
    given()
        .get("/register/cert-email")
        .then()
        .statusCode(400);
  }

  // ===

  @Test
  @Order(3)
  @DisplayName("GET /register/cert-number 200")
  void certNumber_e2e_200() {

    given()
        .param("uuid",
            RegisterE2ETests.uuid)
        .param("number",
            RegisterE2ETests.num)
        .get("/register/cert-number")
        .then()
        .statusCode(200)
        .assertThat()
        .body("success", equalTo(true));

    given()
        .param("uuid",
            "testuuid-1234")
        .param("number",
            "1234")
        .get("/register/cert-number")
        .then()
        .statusCode(200)
        .assertThat()
        .body("success", equalTo(false));
  }

  @Test
  @Order(3)
  @DisplayName("GET /register/cert-number 400")
  void certNumber_e2e_400() {
    given()
        .get("/register/cert-email")
        .then()
        .statusCode(400);
    given()
        .param("number",
            "1234")
        .get("/register/cert-email")
        .then()
        .statusCode(400);
    given()
        .param("uuid",
            "testuuid-1234")
        .get("/register/cert-email")
        .then()
        .statusCode(400);
  }

  // ===

  ObjectMapper objectMapper = new ObjectMapper();

  private String objToJson(Object obj) {
    try {
      return this.objectMapper.writeValueAsString(obj);
    } catch (Exception e) {
      return "";
    }
  }

  @Test
  @Order(4)
  @DisplayName("POST /register 201")
  void register_e2e_200() {

    String pw = "password";
    String nickname = "회원가입 테스트계정";
    String birth = "2002-08-19";

    RegisterDto rgDto = new RegisterDto(
        pw,
        nickname,
        birth,
        RegisterE2ETests.uuid,
        null);

    given()
        .contentType("application/json")
        .body(this.objToJson(rgDto))
        .post("/register")
        .then()
        .statusCode(201);
  }

  @Test
  @Order(4)
  @DisplayName("POST /register 400")
  void register_e2e_400() {

    RegisterDto rgDto = new RegisterDto();

    given()
        .contentType("application/json")
        .body(this.objToJson(rgDto))
        .post("/register")
        .then()
        .statusCode(400);
  }

  static String token = "";

  @Test
  @Order(6)
  @DisplayName("POST /secession")
  void secession_e2e() {

    String email = "register@test.com";
    String pw = "password";

    LoginDto lgdto = new LoginDto(email, pw);

    RegisterE2ETests.token = given()
        .contentType("application/json")
        .body(this.objToJson(lgdto))
        .post("/login")
        .then()
        .statusCode(200)
        .extract()
        .path("token");

    given()
        .contentType("application/json")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + RegisterE2ETests.token)
        .body(new SecessionDto("wrong password"))
        .post("/secession")
        .then()
        .statusCode(400);

    given()
        .contentType("application/json")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + RegisterE2ETests.token)
        .body(new SecessionDto(pw))
        .post("/secession")
        .then()
        .statusCode(200);
  }
}
