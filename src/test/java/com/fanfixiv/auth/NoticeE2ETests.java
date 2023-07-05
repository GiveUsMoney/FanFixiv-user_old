package com.fanfixiv.auth;

import static io.restassured.RestAssured.*;
// import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import com.fanfixiv.auth.entity.NoticeEntity;
import com.fanfixiv.auth.entity.ProfileEntity;
import com.fanfixiv.auth.entity.UserEntity;
import com.fanfixiv.auth.repository.jpa.NoticeRepository;
import com.fanfixiv.auth.repository.jpa.UserRepository;
import com.fanfixiv.auth.repository.redis.RedisLoginRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.response.Response;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoticeE2ETests {

  @LocalServerPort
  private int _port;

  @BeforeEach
  public void setUp() throws Exception {
    port = _port;
  }

  @Autowired
  NoticeRepository noticeRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RedisLoginRepository redisLoginRepository;

  @Autowired
  BCryptPasswordEncoder passwordEncoder;

  static UserEntity user;

  ObjectMapper objectMapper = new ObjectMapper();

  static String accessToken = "";
  static String refreshToken = "";
  static Long seq = 0L;

  private String objToJson(Object obj) {
    try {
      return this.objectMapper.writeValueAsString(obj);
    } catch (Exception e) {
      return "";
    }
  }

  @Test
  @Order(1)
  @DisplayName("GET /notice 200")
  void doNotice_e2e_200() {

    String email = "notice@example.com";
    String pw = "password";
    String nick = "개인 알림 테스트계정";

    ProfileEntity profile = ProfileEntity.builder()
        .nickname(nick)
        .birth(LocalDate.of(2002, 8, 19))
        .build();

    NoticeE2ETests.user = UserEntity.builder()
        .email(email)
        .pw(pw)
        .profile(profile)
        .build();

    NoticeE2ETests.user.hashPassword(passwordEncoder);

    userRepository.save(user);

    LoginDto lgdto = new LoginDto(email, pw);

    redisLoginRepository.deleteById(email);

    NoticeE2ETests.accessToken = given()
        .contentType("application/json")
        .body(this.objToJson(lgdto))
        .post("/login")
        .then()
        .statusCode(200)
        .extract()
        .path("token");

    noticeRepository.save(
        NoticeEntity.builder()
            .content("테스트 공지사항입니다.")
            .toAll(true)
            .build());

    given()
        .contentType("application/json")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + NoticeE2ETests.accessToken)
        .get("/notice")
        .then()
        .statusCode(200)
        .assertThat()
        .body("last().message", equalTo("테스트 공지사항입니다."));
  }

  @Test
  @Order(1)
  @DisplayName("GET /notice 401")
  void doNotice_e2e_401() {
    given()
        .contentType("application/json")
        .header(HttpHeaders.AUTHORIZATION, "Bearer TESTTOKEN")
        .get("/notice")
        .then()
        .statusCode(401);
  }

  @Test
  @Order(2)
  @DisplayName("GET /notice/personal 200")
  void doNoticePersonal_e2e_200() {
    noticeRepository.save(
        NoticeEntity.builder()
            .content("테스트 개인알림입니다.")
            .toAll(false)
            .user(NoticeE2ETests.user)
            .build());

    Response res = given()
        .contentType("application/json")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + NoticeE2ETests.accessToken)
        .get("/notice/personal");

    assertEquals(res.statusCode(), 200);
    assertEquals(res.path("last().message"), "테스트 개인알림입니다.");

    int tmp = res.path("last().seq");

    NoticeE2ETests.seq = Long.valueOf(tmp);
  }

  @Test
  @Order(1)
  @DisplayName("GET /notice/personal 401")
  void doNoticePersonal_e2e_401() {
    given()
        .contentType("application/json")
        .header(HttpHeaders.AUTHORIZATION, "Bearer TESTTOKEN")
        .get("/notice/personal")
        .then()
        .statusCode(401);
  }

  @Test
  @Order(3)
  @DisplayName("PUT /notice/personal 200")
  void doNoticePersonalCheck_e2e_200() {

    NoticeEntity nEntity = noticeRepository.findById(seq).orElseThrow();

    assertEquals(nEntity.isChecked(), false);

    given()
        .contentType("application/json")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + NoticeE2ETests.accessToken)
        .param("seq", NoticeE2ETests.seq)
        .put("/notice/personal")
        .then()
        .statusCode(200);

    nEntity = noticeRepository.findById(seq).orElseThrow();

    assertEquals(nEntity.isChecked(), true);
  }
}
