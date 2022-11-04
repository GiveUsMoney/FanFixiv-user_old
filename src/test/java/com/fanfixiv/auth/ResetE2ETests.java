package com.fanfixiv.auth;

import static io.restassured.RestAssured.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;

import java.time.LocalDate;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fanfixiv.auth.dto.login.LoginDto;
import com.fanfixiv.auth.dto.register.CertEmailDto;
import com.fanfixiv.auth.dto.reset.ResetTokenDto;
import com.fanfixiv.auth.entity.ProfileEntity;
import com.fanfixiv.auth.entity.UserEntity;
import com.fanfixiv.auth.repository.jpa.UserRepository;
import com.fanfixiv.auth.service.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ResetE2ETests {

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

  @MockBean
  MailService mailService;

  static String uuid = "";
  static UserEntity user;

  ObjectMapper objectMapper = new ObjectMapper();

  private String objToJson(Object obj) {
    try {
      return this.objectMapper.writeValueAsString(obj);
    } catch (Exception e) {
      return "";
    }
  }

  @Test
  @Order(1)
  @DisplayName("POST /reset/email 200")
  void resetEmail_e2e_200() {

    String email = "reset@example.com";
    String pw = "password";
    String nick = "비밀번호 초기화 테스트계정";

    ProfileEntity profile = ProfileEntity.builder()
        .nickname(nick)
        .birth(LocalDate.of(2002, 8, 19))
        .build();

    ResetE2ETests.user = UserEntity.builder()
        .email(email)
        .pw(pw)
        .profile(profile)
        .build();

    user.hashPassword(passwordEncoder);

    userRepository.save(user);

    doAnswer(
        new Answer<Object>() {
          public Object answer(InvocationOnMock invocation) {
            ResetE2ETests.uuid = (String) invocation.getArgument(0);
            return null;
          }
        })
        .when(mailService)
        .sendResetPwMail(anyString(), anyList());

    CertEmailDto dto = new CertEmailDto(email);

    given()
        .contentType("application/json")
        .body(this.objToJson(dto))
        .post("/reset/email")
        .then()
        .statusCode(200);
  }

  @Test
  @Order(1)
  @DisplayName("POST /reset/email 400")
  void resetEmail_e2e_400() {
    CertEmailDto dto = new CertEmailDto("noemail@example.com");
    given()
        .contentType("application/json")
        .body(this.objToJson(dto))
        .post("/reset/email")
        .then()
        .statusCode(400);
  }

  // ===

  @Test
  @Order(2)
  @DisplayName("POST /reset/pw 200")
  void resetPW_e2e_200() {

    String email = "reset@example.com";
    String pw = "new_password";

    ResetTokenDto dto = new ResetTokenDto(ResetE2ETests.uuid, pw);

    given()
        .contentType("application/json")
        .body(this.objToJson(dto))
        .post("/reset/pw")
        .then()
        .statusCode(200);

    LoginDto lgdto = new LoginDto(email, pw);

    given()
        .contentType("application/json")
        .body(this.objToJson(lgdto))
        .post("/login")
        .then()
        .statusCode(200);
  }

  @Test
  @Order(2)
  @DisplayName("POST /reset/pw 400")
  void resetPW_e2e_400() {

    String pw = "new_password";

    ResetTokenDto dto = new ResetTokenDto("TESTUUID", pw);

    given()
        .contentType("application/json")
        .body(this.objToJson(dto))
        .post("/reset/pw")
        .then()
        .statusCode(400);
  }
}
