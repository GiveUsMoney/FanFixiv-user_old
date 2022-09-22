package com.fanfixiv.auth.controller;

import com.fanfixiv.auth.dto.TempEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = {"임시 API (후일 테스트와 함께 삭제해주세요)"})
public class TempController {

  private static int ID = 0;

  private final RedisTemplate<String, String> redisTemplate;

  @GetMapping("/")
  @ApiOperation(value = "임시 API")
  public TempEntity temporary(@RequestParam(defaultValue = "Hello World!!!") String content) {
    return new TempEntity(ID++, content);
  }

  @GetMapping("/redis")
  @ApiOperation(value = "임시 API")
  public TempEntity redisTest() {
    redisTemplate.opsForValue().set("Hello", "World!");
    return new TempEntity(ID++, "TEST");
  }
}
