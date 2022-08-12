package com.fanfixiv.auth.controller;

import com.fanfixiv.auth.domain.TempEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {

  private static int ID = 0;

  @GetMapping("/")
  public TempEntity temporary(@RequestParam(defaultValue = "Hello World!") String name) {
    return new TempEntity(ID++, name);
  }
}
