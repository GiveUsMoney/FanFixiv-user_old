package com.fanfixiv.auth;

import static io.restassured.RestAssured.*;
// import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import com.fanfixiv.auth.entity.ProfileEntity;
import com.fanfixiv.auth.repository.ProfileRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthE2ETests {

  @Autowired private ProfileRepository profileRepository;

  @LocalServerPort private int _port;

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

  @Test
  @DisplayName("H2 데이터 베이스 테스트")
  void h2test() {
    ProfileEntity _p = ProfileEntity.builder().nickname("테스트").descript("테스트입니다.").build();

    profileRepository.save(_p);

    ProfileEntity p = profileRepository.findAll().get(0);

    assertEquals(_p.getNickname(), p.getNickname()); 
  }
}
