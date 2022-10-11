package com.fanfixiv.auth;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.fanfixiv.auth.entity.ProfileEntity;
import com.fanfixiv.auth.repository.ProfileRepository;
import com.fanfixiv.auth.service.MailService;

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
  @DisplayName("GET /register/cert-email 200")
  void certEmail_e2e_200() {
    
    doAnswer(
      new Answer<Object>() {
      public Object answer(InvocationOnMock invocation) {
          RegisterE2ETests.num = (String)invocation.getArgument(1);
          return null;
      }})
    .when(mailService)
    .sendMail(anyString(), anyString(), anyList());
    
    RegisterE2ETests.uuid = given()
        .param("email", "test@example.com")
        .get("/register/cert-email")
        .then()
        .statusCode(200)
        .extract()
        .path("uuid");
  }

  @Test
  @DisplayName("GET /register/cert-email 400")
  void certEmail_e2e_400() {
    given()
        .get("/register/cert-email")
        .then()
        .statusCode(400);
  }

  // ===

  @Test
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
}
